package ru.g000sha256.reduktor

import io.reactivex.Flowable
import io.reactivex.processors.BehaviorProcessor
import ru.g000sha256.reduktor.actions.Initializer
import ru.g000sha256.reduktor.actions.Reducer
import ru.g000sha256.reduktor.actions.SideEffect
import ru.g000sha256.reduktor.states.Middleware

fun <A, S> RxJavaReduktor(
    initialState: S,
    reducer: Reducer<A, S>,
    initializers: Iterable<Initializer<A, S>> = emptyList(),
    sideEffects: Iterable<SideEffect<A, S>> = emptyList(),
    logger: Logger = Logger {}
): RxJavaReduktor<A, S> {
    val behaviorProcessor = BehaviorProcessor.createDefault(initialState)
    val reduktor = Reduktor(initialState, reducer, initializers, sideEffects, logger, behaviorProcessor::onNext)
    return RxJavaReduktorImpl(behaviorProcessor, reduktor)
}

fun <A, S> RxJavaReduktor(
    initialState: S,
    middlewares: Iterable<Middleware<A, S>> = emptyList(),
    logger: Logger = Logger {}
): RxJavaReduktor<A, S> {
    val behaviorProcessor = BehaviorProcessor.createDefault(initialState)
    val reduktor = Reduktor(initialState, middlewares, logger, behaviorProcessor::onNext)
    return RxJavaReduktorImpl(behaviorProcessor, reduktor)
}

private class RxJavaReduktorImpl<A, S>(
    override val states: Flowable<S>,
    reduktor: Reduktor<A, S>
) : RxJavaReduktor<A, S>, Reduktor<A, S> by reduktor