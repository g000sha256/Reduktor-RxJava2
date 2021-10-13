package g000sha256.reduktor.rxjava2

import g000sha256.reduktor.core.Initializer
import g000sha256.reduktor.core.Logger
import g000sha256.reduktor.core.Reducer
import g000sha256.reduktor.core.SideEffect
import g000sha256.reduktor.core.Store
import io.reactivex.Flowable
import io.reactivex.processors.BehaviorProcessor

class Store<A, S>(
    initialState: S,
    reducer: Reducer<A, S>,
    initializers: Iterable<Initializer<A, S>> = ArrayList(),
    sideEffects: Iterable<SideEffect<A, S>> = ArrayList(),
    logger: Logger = Logger {}
) {

    val states: Flowable<S>

    init {
        val behaviorProcessor = BehaviorProcessor.createDefault(initialState)
        states = behaviorProcessor.onBackpressureLatest()
        Store(initialState, reducer, initializers, sideEffects, logger, behaviorProcessor::onNext)
    }

}