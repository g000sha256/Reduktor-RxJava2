package ru.g000sha256.reduktor.actions

import ru.g000sha256.reduktor.Logger
import ru.g000sha256.reduktor.Reduktor

abstract class ActionsReduktor<A, S>(
    initialState: S,
    private val reducer: Reducer<A, S>,
    private val initializers: Iterable<Initializer<A, S>> = emptyList(),
    private val sideEffects: Iterable<SideEffect<A, S>> = emptyList(),
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