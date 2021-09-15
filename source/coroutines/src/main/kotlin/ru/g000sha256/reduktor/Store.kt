package ru.g000sha256.reduktor

import kotlin.coroutines.CoroutineContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineStart
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

    private val stateLock = Any()
    private val subscriptionLock = Any()
    private val dispatcher: Dispatcher<A>
    private val jobs = Jobs()
    private val actionsMutableSharedFlow = MutableSharedFlow<A>()
    private val statesMutableStateFlow = MutableStateFlow(initialState)

    private var subscriptions = 0
    private var state = initialState

    init {
        dispatcher = Dispatcher(scope, actionsMutableSharedFlow, jobs)
        states = statesMutableStateFlow
            .onCompletion { synchronized(subscriptionLock) { if (--subscriptions == 0) jobs.clear() } }
            .onStart { synchronized(subscriptionLock) { if (++subscriptions == 1) subscribe() } }
    }

    private fun handleAction(action: A) {
        val logger = logger
        logger?.invoke("ACTION --> $action")
        val oldState = state
        logger?.invoke("STATE  --> $oldState")
        val newState = reducer.invoke(action, oldState)
        if (newState == oldState) {
            logger?.invoke("STATE      NOT CHANGED")
        } else {
            state = newState
            logger?.invoke("STATE  <-- $newState")
            statesMutableStateFlow.value = newState
        }
        sideEffects.forEach { it.apply { dispatcher.invoke(action, newState) } }
    }

    private fun subscribe() {
        val coroutineContext = context ?: ImmediateCoroutineDispatcher
        jobs += scope.launch(coroutineContext) {
            actionsMutableSharedFlow.collect { synchronized(stateLock) { handleAction(it) } }
        }
        val job = scope.launch(coroutineContext, CoroutineStart.LAZY) {
            synchronized(stateLock) {
                val state = state
                initializers.forEach { it.apply { dispatcher.invoke(state) } }
            }
        }
        job.invokeOnCompletion { jobs -= job }
        jobs += job
        job.start()
    }

}