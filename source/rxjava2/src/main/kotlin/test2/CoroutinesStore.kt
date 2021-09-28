package test2

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import test.Logger
import test.Reducer
import test.SideEffect
import test.Store

class CoroutinesStore<A, S>(
    initialState: S,
    reducer: Reducer<A, S>,
    sideEffects: Iterable<SideEffect<A, S>> = emptyList(),
    logger: Logger = Logger {}
) {

    val states: Flow<S>

    private val store = Store(initialState, reducer, sideEffects, logger)

    init {
        val mutableStateFlow = MutableStateFlow(initialState)
        states = mutableStateFlow
        store.onNewStateCallback = { mutableStateFlow.value = it }
    }

    fun start() {
        store.start()
    }

    fun stop() {
        store.stop()
    }

}