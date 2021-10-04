package ru.g000sha256.reduktor

import io.reactivex.Flowable
import io.reactivex.processors.BehaviorProcessor

internal class RxJavaStoreImpl<A, S>(
    initializers: Iterable<Initializer<A, S>>,
    sideEffects: Iterable<SideEffect<A, S>>,
    logger: Logger,
    reducer: Reducer<A, S>,
    state: S
) : RxJavaStore<A, S> {

    override val states: Flowable<S>

    private val store: Store<A>

    init {
        val behaviorProcessor = BehaviorProcessor.createDefault(state)
        states = behaviorProcessor.onBackpressureLatest()
        store = Store(state, reducer, initializers, sideEffects, logger, behaviorProcessor::onNext)
    }

    override fun post(action: A) {
        store.post(action)
    }

}