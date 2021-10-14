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
    initializers: Collection<Initializer<A, S>> = ArrayList(),
    sideEffects: Collection<SideEffect<A, S>> = ArrayList(),
    logger: Logger = Logger {}
) {

    val states: Flowable<S>

    init {
        val behaviorProcessor = BehaviorProcessor.createDefault(initialState)
        Store(initialState, reducer, initializers, sideEffects, logger, behaviorProcessor::onNext)
        states = behaviorProcessor.onBackpressureLatest()
    }

}