package ru.g000sha256.reduktor.states

import ru.g000sha256.reduktor.Logger
import ru.g000sha256.reduktor.Reduktor

abstract class StatesReduktor<A, S>(
    initialState: S,
    private val middlewares: Iterable<Middleware<A, S>> = emptyList(),
    private val logger: Logger = Logger {}
) : Reduktor<A, S>() {

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