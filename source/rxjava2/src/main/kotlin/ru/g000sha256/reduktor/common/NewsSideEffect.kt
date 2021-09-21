package ru.g000sha256.reduktor.common

import io.reactivex.Flowable
import io.reactivex.processors.PublishProcessor
import ru.g000sha256.reduktor.Dispatcher
import ru.g000sha256.reduktor.SideEffect

abstract class NewsSideEffect<A, S, T> : SideEffect<A, S> {

    val news: Flowable<T>
        get() = publishProcessor

    private val publishProcessor = PublishProcessor.create<T>()

    final override fun Dispatcher<A>.invoke(action: A, state: S) {
        val result = map(action, state) ?: return
        publishProcessor.onNext(result)
    }

    protected abstract fun map(action: A, state: S): T?

}