package g000sha256.reduktor.coroutines

import g000sha256.reduktor.core.Initializer
import g000sha256.reduktor.core.Logger
import g000sha256.reduktor.core.Reducer
import g000sha256.reduktor.core.SideEffect
import g000sha256.reduktor.core.Store
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class Store<A, S>(
    initialState: S,
    reducer: Reducer<A, S>,
    initializers: List<Initializer<A, S>> = ArrayList(),
    sideEffects: List<SideEffect<A, S>> = ArrayList(),
    logger: Logger = Logger {}
) {

    val states: Flow<S>

    init {
        val mutableStateFlow = MutableStateFlow(initialState)
        Store(initialState, reducer, initializers, sideEffects, logger) { mutableStateFlow.value = it }
        states = mutableStateFlow
    }

}