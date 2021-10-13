package ru.g000sha256.reduktor.coroutines

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import ru.g000sha256.reduktor.core.Initializer
import ru.g000sha256.reduktor.core.Logger
import ru.g000sha256.reduktor.core.Reducer
import ru.g000sha256.reduktor.core.SideEffect
import ru.g000sha256.reduktor.core.Store

class Store<A, S>(
    initialState: S,
    reducer: Reducer<A, S>,
    initializers: Iterable<Initializer<A, S>> = ArrayList(),
    sideEffects: Iterable<SideEffect<A, S>> = ArrayList(),
    logger: Logger = Logger {}
) {

    val states: Flow<S>

    init {
        val mutableStateFlow = MutableStateFlow(initialState)
        states = mutableStateFlow
        Store(initialState, reducer, initializers, sideEffects, logger) { mutableStateFlow.value = it }
    }

}