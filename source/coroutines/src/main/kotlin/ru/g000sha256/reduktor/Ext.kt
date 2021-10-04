package ru.g000sha256.reduktor

fun <A, S> CoroutinesStore(
    state: S,
    reducer: Reducer<A, S>,
    initializers: Iterable<Initializer<A, S>> = emptyList(),
    sideEffects: Iterable<SideEffect<A, S>> = emptyList(),
    logger: Logger = Logger {}
): CoroutinesStore<A, S> {
    return CoroutinesStoreImpl(initializers, sideEffects, logger, reducer, state)
}