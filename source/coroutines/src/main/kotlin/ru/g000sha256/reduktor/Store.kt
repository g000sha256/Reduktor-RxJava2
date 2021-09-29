package ru.g000sha256.reduktor

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class Store<A, S>(
    initialState: S,
    private val reducer: Reducer<A, S>,
    private val initializers: Iterable<Initializer<A, S>> = emptyList(),
    private val sideEffects: Iterable<SideEffect<A, S>> = emptyList(),
    private val logger: Logger = Logger {}
) {

    val states: Flow<S>

    private val mutableStatFlow = MutableStateFlow(initialState)

    init {
        states = mutableStatFlow
    }

    fun start() {
        // ToDo
    }

    fun stop() {
        // ToDo
    }

}