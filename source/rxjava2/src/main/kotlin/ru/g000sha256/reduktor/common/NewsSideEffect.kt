package ru.g000sha256.reduktor.common

import io.reactivex.Flowable
import io.reactivex.processors.PublishProcessor
import ru.g000sha256.reduktor.SideEffect

abstract class NewsSideEffect<A, S, T> : SideEffect<A, S> {

    val news: Flowable<T>

    private val publishProcessor = PublishProcessor.create<T>()

    init {
        news = publishProcessor
    }

    override fun SideEffect.Context<A>.invoke(action: A, state: S) {
        val result = map(action, state) ?: return
        publishProcessor.onNext(result)
    }

    protected abstract fun map(action: A, state: S): T?

}