package ru.g000sha256.reduktor

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

internal class CoroutinesStoreImpl<A, S>(
    initializers: Iterable<Initializer<A, S>>,
    sideEffects: Iterable<SideEffect<A, S>>,
    logger: Logger,
    reducer: Reducer<A, S>,
    state: S
) : CoroutinesStore<A, S> {

    override val states: Flow<S>

    private val store: Store<A>

    init {
        val mutableStateFlow = MutableStateFlow(state)
        states = mutableStateFlow
        store = Store(state, reducer, initializers, sideEffects, logger) { mutableStateFlow.value = it }
    }

    override fun post(action: A) {
        store.post(action)
    }

}