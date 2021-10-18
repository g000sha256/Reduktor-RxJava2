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
    initializers: List<Initializer<A, S>> = emptyList(),
    sideEffects: List<SideEffect<A, S>> = emptyList(),
    logger: Logger = Logger {}
) {

    val states: Flow<S>

    private val store: Store<A, S>

    init {
        val mutableStateFlow = MutableStateFlow(initialState)
        store = Store(initialState, reducer, initializers, sideEffects, logger) { mutableStateFlow.value = it }
        states = mutableStateFlow
    }

    fun release() {
        store.release()
    }

}