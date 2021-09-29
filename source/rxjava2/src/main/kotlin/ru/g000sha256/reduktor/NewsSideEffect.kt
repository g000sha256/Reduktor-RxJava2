package ru.g000sha256.reduktor

import io.reactivex.Flowable
import io.reactivex.processors.PublishProcessor

abstract class NewsSideEffect<A, S, T> : SideEffect<A, S> {

    val news: Flowable<T>
        get() = publishProcessor

    private val publishProcessor = PublishProcessor.create<T>()

    override fun SideEffect.Context<A>.invoke(action: A, state: S) {
        val result = map(action, state) ?: return
        publishProcessor.onNext(result)
    }

    protected abstract fun map(action: A, state: S): T?

}