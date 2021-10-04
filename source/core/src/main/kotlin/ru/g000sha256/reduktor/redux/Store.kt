package ru.g000sha256.reduktor.redux

import ru.g000sha256.reduktor.Logger

abstract class Store<A, S>(
    initialState: S,
    private val reducer: Reducer<A, S>,
    private val initializers: Iterable<Initializer<A, S>> = emptyList(),
    private val sideEffects: Iterable<SideEffect<A, S>> = emptyList(),
    private val logger: Logger = Logger {}
) {

    protected abstract fun onNewState(state: S)

    fun dispatch(action: A) {
        // ToDo
    }

    fun start() {
        // ToDo
    }

    fun stop() {
        // ToDo
    }

}