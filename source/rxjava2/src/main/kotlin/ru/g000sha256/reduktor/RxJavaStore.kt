package ru.g000sha256.reduktor

import io.reactivex.Flowable
import io.reactivex.processors.BehaviorProcessor

class RxJavaStore<A, S>(
    initialState: S,
    reducer: Reducer<A, S>,
    initializers: Iterable<Initializer<A, S>> = emptyList(),
    sideEffects: Iterable<SideEffect<A, S>> = emptyList(),
    logger: Logger = Logger {}
) : Store<A, S>(initialState, reducer, initializers, sideEffects, logger) {

    val states: Flowable<S>

    private val behaviorProcessor = BehaviorProcessor.createDefault(initialState)

    init {
        states = behaviorProcessor.onBackpressureLatest()
    }

    override fun onNewState(state: S) {
        behaviorProcessor.onNext(state)
    }

}