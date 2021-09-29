package ru.g000sha256.reduktor

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class CoroutinesStore<A, S>(
    initialState: S,
    reducer: Reducer<A, S>,
    initializers: Iterable<Initializer<A, S>> = emptyList(),
    sideEffects: Iterable<SideEffect<A, S>> = emptyList(),
    logger: Logger = Logger {}
) : Store<A, S>(initialState, reducer, initializers, sideEffects, logger) {

    val states: Flow<S>

    private val mutableStatFlow = MutableStateFlow(initialState)

    init {
        states = mutableStatFlow
    }

    override fun onNewState(state: S) {
        mutableStatFlow.value = state
    }

}