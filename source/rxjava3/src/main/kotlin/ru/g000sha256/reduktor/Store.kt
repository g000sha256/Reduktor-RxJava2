package ru.g000sha256.reduktor

import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Maybe
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.functions.Action
import io.reactivex.rxjava3.processors.BehaviorProcessor
import io.reactivex.rxjava3.processors.PublishProcessor

class Store<A, S>(
    initialState: S,
    private val reducer: Reducer<A, S>,
    private val initializers: List<Initializer<A, S>> = emptyList(),
    private val sideEffects: List<SideEffect<A, S>> = emptyList(),
    private val scheduler: Scheduler? = null,
    private val logger: Logger? = null
) {

    val states: Flowable<S>

    private val behaviorProcessor = BehaviorProcessor.createDefault(initialState)
    private val dispatcher: Dispatcher<A> = DispatcherImpl()
    private val lock = Lock()
    private val mutableList = mutableListOf<Disposable>()
    private val mutableMap = mutableMapOf<String, Disposable>()
    private val publishProcessor = PublishProcessor.create<A>()

    private val thread: String
        get() {
            val thread = Thread.currentThread()
            return thread.name
        }

    private var subscriptions = 0
    private var state = initialState

    init {
        states = behaviorProcessor
            .doOnCancel { stopIfNeeded() }
            .doOnSubscribe { startIfNeeded() }
            .onBackpressureLatest()
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
                behaviorProcessor.onNext(newState)
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
                publishProcessor
                    .run { scheduler?.let { scheduler -> observeOn(scheduler) } ?: this }
                    .doOnNext { handleAction(it) }
                    .ignoreElements()
                    .launch()
                Completable
                    .fromCallable { init() }
                    .run { scheduler?.let { scheduler -> subscribeOn(scheduler) } ?: this }
                    .launch()
            }
        }
    }

    private fun stopIfNeeded() {
        lock.sync {
            if (--subscriptions != 0) return@sync
            mutableList.forEach { it.disposeIfNeeded() }
            mutableList.clear()
            mutableMap.forEach {
                logTaskRemove(it.key)
                it.value.disposeIfNeeded()
            }
            mutableMap.clear()
        }
    }

    private fun Disposable.disposeIfNeeded() {
        if (!isDisposed) dispose()
    }

    private inner class DispatcherImpl : Dispatcher<A> {

        override fun cancel(key: String) {
            lock.sync {
                val disposable = mutableMap.remove(key) ?: return@sync
                logTaskRemove(key)
                disposable.disposeIfNeeded()
            }
        }

        override fun dispatch(action: A) {
            publishProcessor.onNext(action)
        }

        override fun Completable.launch(key: String?) {
            run(key) { action ->
                return@run subscribe(
                    { action.run() },
                    {
                        action.run()
                        publishProcessor.onError(it)
                    }
                )
            }
        }

        override fun Flowable<A>.launch(key: String?) {
            run(key) { action ->
                return@run subscribe(
                    { publishProcessor.onNext(it) },
                    {
                        action.run()
                        publishProcessor.onError(it)
                    },
                    { action.run() }
                )
            }
        }

        override fun Maybe<A>.launch(key: String?) {
            run(key) { action ->
                return@run subscribe(
                    {
                        action.run()
                        publishProcessor.onNext(it)
                    },
                    {
                        action.run()
                        publishProcessor.onError(it)
                    },
                    { action.run() }
                )
            }
        }

        override fun Observable<A>.launch(key: String?) {
            run(key) { action ->
                return@run subscribe(
                    { publishProcessor.onNext(it) },
                    {
                        action.run()
                        publishProcessor.onError(it)
                    },
                    { action.run() }
                )
            }
        }

        override fun Single<A>.launch(key: String?) {
            run(key) { action ->
                return@run subscribe(
                    {
                        action.run()
                        publishProcessor.onNext(it)
                    },
                    {
                        action.run()
                        publishProcessor.onError(it)
                    }
                )
            }
        }

        private fun clear(disposable: Disposable) {
            lock.sync { mutableList -= disposable }
        }

        private fun run(key: String?, callback: (Action) -> Disposable) {
            lock.sync {
                if (key == null) {
                    var disposable: Disposable? = null
                    val action = Action { disposable?.apply { clear(this) } }
                    disposable = callback(action)
                    if (!disposable.isDisposed) mutableList += disposable
                } else {
                    cancel(key)
                    val action = Action { cancel(key) }
                    val disposable = callback(action)
                    if (!disposable.isDisposed) {
                        logTaskAdd(key)
                        mutableMap[key] = disposable
                    }
                }
            }
        }

    }

    private class Lock {

        fun sync(callback: () -> Unit) {
            synchronized(this, callback)
        }

    }

}