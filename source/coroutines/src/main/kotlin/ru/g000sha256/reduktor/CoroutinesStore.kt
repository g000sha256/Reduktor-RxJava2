package ru.g000sha256.reduktor

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class CoroutinesStore<A, S>(
    initialState: S,
    reducer: Reducer<A, S>,
    initializers: Iterable<Initializer<A, S>> = emptyList(),
    sideEffects: Iterable<SideEffect<A, S>> = emptyList(),
    logger: Logger = Logger {}
) {

    val states: Flow<S>

    init {
        val mutableStateFlow = MutableStateFlow(initialState)
        states = mutableStateFlow
        Store(initialState, reducer, initializers, sideEffects, logger) { mutableStateFlow.value = it }
    }

}