package g000sha256.reduktor.rxjava3

import g000sha256.reduktor.core.Initializer
import g000sha256.reduktor.core.Logger
import g000sha256.reduktor.core.Reducer
import g000sha256.reduktor.core.SideEffect
import g000sha256.reduktor.core.Store
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.processors.BehaviorProcessor

class Store<A, S>(
    initialState: S,
    reducer: Reducer<A, S>,
    initializers: List<Initializer<A, S>> = emptyList(),
    sideEffects: List<SideEffect<A, S>> = emptyList(),
    logger: Logger = Logger {}
) {

    val states: Flowable<S>

    private val store: Store<A, S>

    init {
        val behaviorProcessor = BehaviorProcessor.createDefault(initialState)
        store = Store(initialState, reducer, initializers, sideEffects, logger, behaviorProcessor::onNext)
        states = behaviorProcessor.onBackpressureLatest()
    }

    fun release() {
        store.release()
    }

}