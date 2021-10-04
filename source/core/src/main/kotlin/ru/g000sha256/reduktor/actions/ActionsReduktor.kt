package ru.g000sha256.reduktor.actions

import ru.g000sha256.reduktor.Logger
import ru.g000sha256.reduktor.Reduktor

internal class ActionsReduktor<A, S>(
    private val initializers: Iterable<Initializer<A, S>>,
    private val sideEffects: Iterable<SideEffect<A, S>>,
    private val logger: Logger,
    private val reducer: Reducer<A, S>,
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