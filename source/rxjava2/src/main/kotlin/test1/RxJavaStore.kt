package test1

import io.reactivex.Flowable
import io.reactivex.processors.BehaviorProcessor
import test.Logger
import test.Reducer
import test.SideEffect

class RxJavaStore<A, S>(
    initialState: S,
    reducer: Reducer<A, S>,
    sideEffects: Iterable<SideEffect<A, S>> = emptyList(),
    logger: Logger = Logger {}
) {

    val states: Flowable<S>

    private val store = Store(initialState, reducer, sideEffects, logger)

    init {
        val behaviorProcessor = BehaviorProcessor.create<S>()
        states = behaviorProcessor
        store.onNewStateCallback = { behaviorProcessor.onNext(it) }
    }

    fun start() {
        store.start()
    }

    fun stop() {
        store.stop()
    }

}