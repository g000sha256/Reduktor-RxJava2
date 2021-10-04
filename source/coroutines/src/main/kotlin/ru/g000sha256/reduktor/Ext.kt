package ru.g000sha256.reduktor

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import ru.g000sha256.reduktor.actions.Initializer
import ru.g000sha256.reduktor.actions.Reducer
import ru.g000sha256.reduktor.actions.SideEffect
import ru.g000sha256.reduktor.states.Middleware

fun <A, S> CoroutinesReduktor(
    initialState: S,
    reducer: Reducer<A, S>,
    initializers: Iterable<Initializer<A, S>> = emptyList(),
    sideEffects: Iterable<SideEffect<A, S>> = emptyList(),
    logger: Logger = Logger {}
): CoroutinesReduktor<A, S> {
    val mutableStateFlow = MutableStateFlow(initialState)
    val reduktor = Reduktor(initialState, reducer, initializers, sideEffects, logger) { mutableStateFlow.value = it }
    return CoroutinesReduktorImpl(mutableStateFlow, reduktor)
}

fun <A, S> CoroutinesReduktor(
    initialState: S,
    middlewares: Iterable<Middleware<A, S>> = emptyList(),
    logger: Logger = Logger {}
): CoroutinesReduktor<A, S> {
    val mutableStateFlow = MutableStateFlow(initialState)
    val reduktor = Reduktor(initialState, middlewares, logger) { mutableStateFlow.value = it }
    return CoroutinesReduktorImpl(mutableStateFlow, reduktor)
}

private class CoroutinesReduktorImpl<A, S>(
    override val states: Flow<S>,
    reduktor: Reduktor<A>
) : CoroutinesReduktor<A, S>, Reduktor<A> by reduktor