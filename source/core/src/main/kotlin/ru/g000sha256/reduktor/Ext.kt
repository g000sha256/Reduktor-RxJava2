package ru.g000sha256.reduktor

import ru.g000sha256.reduktor.actions.ActionsReduktor
import ru.g000sha256.reduktor.actions.Initializer
import ru.g000sha256.reduktor.actions.Reducer
import ru.g000sha256.reduktor.actions.SideEffect
import ru.g000sha256.reduktor.states.Middleware
import ru.g000sha256.reduktor.states.StatesReduktor

fun <A, S> Reduktor(
    initialState: S,
    reducer: Reducer<A, S>,
    initializers: Iterable<Initializer<A, S>> = emptyList(),
    sideEffects: Iterable<SideEffect<A, S>> = emptyList(),
    logger: Logger = Logger {},
    onNewState: (state: S) -> Unit
): Reduktor<A, S> {
    return ActionsReduktor(initializers, sideEffects, logger, reducer, onNewState, initialState)
}

fun <A, S> Reduktor(
    initialState: S,
    middlewares: Iterable<Middleware<A, S>> = emptyList(),
    logger: Logger = Logger {},
    onNewState: (state: S) -> Unit
): Reduktor<A, S> {
    return StatesReduktor(middlewares, logger, onNewState, initialState)
}