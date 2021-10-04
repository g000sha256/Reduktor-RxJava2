package ru.g000sha256.reduktor.states

import ru.g000sha256.reduktor.Logger
import ru.g000sha256.reduktor.Reduktor

internal class StatesReduktor<A, S>(
    private val middlewares: Iterable<Middleware<A, S>>,
    private val logger: Logger,
    private val onNewState: (S) -> Unit,
    initialState: S
) : Reduktor<A, S> {

    override fun dispatch(action: A) {
        // ToDo
    }

    override fun start() {
        // ToDo
    }

    override fun stop() {
        // ToDo
    }

}