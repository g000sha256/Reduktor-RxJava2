package ru.g000sha256.reduktor.state_machine

import ru.g000sha256.reduktor.Logger

abstract class StateMachine<A, S>(
    initialState: S,
    private val middlewares: Iterable<Middleware<A, S>> = emptyList(),
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