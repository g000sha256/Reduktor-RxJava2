package ru.g000sha256.reduktor

import io.reactivex.Flowable
import io.reactivex.processors.BehaviorProcessor

class Store<A, S>(
    initialState: S,
    private val reducer: Reducer<A, S>,
    private val initializers: Iterable<Initializer<A, S>> = emptyList(),
    private val sideEffects: Iterable<SideEffect<A, S>> = emptyList(),
    private val logger: Logger = Logger {}
) {

    val states: Flowable<S>

    private val behaviorProcessor = BehaviorProcessor.createDefault(initialState)

    init {
        states = behaviorProcessor.onBackpressureLatest()
    }

    fun start() {
        // ToDo
    }

    fun stop() {
        // ToDo
    }

}