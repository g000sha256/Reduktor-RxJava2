package ru.g000sha256.reduktor

import io.reactivex.Flowable
import io.reactivex.processors.BehaviorProcessor

class RxJavaStore<A, S>(
    initialState: S,
    reducer: Reducer<A, S>,
    initializers: Iterable<Initializer<A, S>> = emptyList(),
    sideEffects: Iterable<SideEffect<A, S>> = emptyList(),
    logger: Logger = Logger {}
) {

    val states: Flowable<S>

    init {
        val behaviorProcessor = BehaviorProcessor.createDefault(initialState)
        states = behaviorProcessor.onBackpressureLatest()
        Store(initialState, reducer, initializers, sideEffects, logger, behaviorProcessor::onNext)
    }

}