package ru.g000sha256.reduktor.rxjava2

import io.reactivex.Flowable
import io.reactivex.processors.BehaviorProcessor
import ru.g000sha256.reduktor.core.Initializer
import ru.g000sha256.reduktor.core.Logger
import ru.g000sha256.reduktor.core.Reducer
import ru.g000sha256.reduktor.core.SideEffect
import ru.g000sha256.reduktor.core.Store

class Store<A, S>(
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