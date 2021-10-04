package ru.g000sha256.reduktor

fun <A, S> Store(
    state: S,
    reducer: Reducer<A, S>,
    initializers: Iterable<Initializer<A, S>> = emptyList(),
    sideEffects: Iterable<SideEffect<A, S>> = emptyList(),
    logger: Logger = Logger {},
    onNewState: (state: S) -> Unit
): Store<A> {
    return StoreImpl(initializers, sideEffects, logger, reducer, onNewState, state)
}