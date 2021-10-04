package ru.g000sha256.reduktor

fun <A, S> RxJavaStore(
    initialState: S,
    reducer: Reducer<A, S>,
    initializers: Iterable<Initializer<A, S>> = emptyList(),
    sideEffects: Iterable<SideEffect<A, S>> = emptyList(),
    logger: Logger = Logger {}
): RxJavaStore<A, S> {
    return RxJavaStoreImpl(initializers, sideEffects, logger, reducer, initialState)
}