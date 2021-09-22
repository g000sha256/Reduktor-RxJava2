package ru.g000sha256.reduktor

import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class Store<A, S>(
    initialState: S,
    private val reducer: Reducer<A, S>,
    private val initializers: List<Initializer<A, S>> = emptyList(),
    private val sideEffects: List<SideEffect<A, S>> = emptyList(),
    private val scope: CoroutineScope,
    private val context: CoroutineContext? = null,
    private val logger: Logger? = null
) {

    val states: Flow<S>

    private val dispatcher: Dispatcher<A> = DispatcherImpl()
    private val lock = Lock()
    private val mutableList = mutableListOf<Job>()
    private val mutableMap = mutableMapOf<String, Job>()
    private val mutableSharedFlow = MutableSharedFlow<A>()
    private val mutableStateFlow = MutableStateFlow(initialState)

    private val thread: String
        get() {
            val thread = Thread.currentThread()
            return thread.name
        }

    private var subscriptions = 0
    private var state = initialState

    init {
        states = mutableStateFlow
            .onCompletion { stopIfNeeded() }
            .onStart { startIfNeeded() }
    }

    private fun handleAction(action: A) {
        lock.sync {
            val oldState = state
            logActionStart(action, oldState)
            val newState = reducer.invoke(action, oldState)
            if (newState == oldState) {
                logActionEnd()
            } else {
                state = newState
                logActionEnd(newState)
                mutableStateFlow.value = newState
            }
            sideEffects.forEach { it.apply { dispatcher.invoke(action, newState) } }
        }
    }

    private fun init() {
        lock.sync {
            val state = state
            initializers.forEach { it.apply { dispatcher.invoke(state) } }
        }
    }

    private fun logActionEnd() {
        logger?.apply {
            invoke("| STATE    NOT CHANGED")
            invoke("| THREAD   $thread")
            invoke("|----------")
        }
    }

    private fun logActionEnd(state: S) {
        logger?.apply {
            invoke("| STATE  < $state")
            invoke("| THREAD   $thread")
            invoke("|----------")
        }
    }

    private fun logActionStart(action: A, state: S) {
        logger?.apply {
            invoke("|----------")
            invoke("| ACTION > $action")
            invoke("| STATE  > $state")
        }
    }

    private fun logTaskAdd(key: String) {
        logger?.apply {
            invoke("|----------")
            invoke("| TASK   + $key")
            invoke("| THREAD   $thread")
            invoke("|----------")
        }
    }

    private fun logTaskRemove(key: String) {
        logger?.apply {
            invoke("|----------")
            invoke("| TASK   - $key")
            invoke("| THREAD   $thread")
            invoke("|----------")
        }
    }

    private fun startIfNeeded() {
        lock.sync {
            if (++subscriptions != 1) return@sync
            dispatcher.apply {
                val coroutineContext = context ?: Dispatchers.Unconfined
                launch(coroutineContext) { mutableSharedFlow.collect { handleAction(it) } }
                launch(coroutineContext) { init() }
            }
        }
    }

    private fun stopIfNeeded() {
        lock.sync {
            if (--subscriptions != 0) return@sync
            mutableList.forEach { it.cancelIfNeeded() }
            mutableList.clear()
            mutableMap.forEach {
                logTaskRemove(it.key)
                it.value.cancelIfNeeded()
            }
            mutableMap.clear()
        }
    }

    private fun Job.cancelIfNeeded() {
        if (isActive) {
            val cancellationException = InternalCancellationException()
            cancel(cancellationException)
        }
    }

    private inner class DispatcherImpl : Dispatcher<A> {

        override fun cancel(key: String) {
            lock.sync {
                val job = mutableMap.remove(key) ?: return@sync
                logTaskRemove(key)
                job.cancelIfNeeded()
            }
        }

        override fun dispatch(action: A) {
            scope.launch(Dispatchers.Unconfined) { mutableSharedFlow.emit(action) }
        }

        override fun launch(context: CoroutineContext?, key: String?, callback: suspend CoroutineScope.() -> Unit) {
            lock.sync {
                val job = scope.launch(Dispatchers.Unconfined, CoroutineStart.LAZY) {
                    launch(context ?: EmptyCoroutineContext, block = callback)
                }
                if (key == null) {
                    job.invokeOnCompletion {
                        if (it is InternalCancellationException) return@invokeOnCompletion
                        clear(job)
                    }
                    mutableList += job
                } else {
                    cancel(key)
                    job.invokeOnCompletion {
                        if (it is InternalCancellationException) return@invokeOnCompletion
                        cancel(key)
                    }
                    logTaskAdd(key)
                    mutableMap[key] = job
                }
                job.start()
            }
        }

        override fun Flow<A>.launch(context: CoroutineContext?, key: String?) {
            launch(context, key) { collect { dispatch(it) } }
        }

        private fun clear(job: Job) {
            lock.sync { mutableList -= job }
        }

    }

    private class InternalCancellationException : CancellationException()

    private class Lock {

        fun sync(callback: () -> Unit) {
            synchronized(this, callback)
        }

    }

}