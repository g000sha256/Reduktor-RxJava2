package ru.g000sha256.reduktor.common

import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.processors.PublishProcessor
import ru.g000sha256.reduktor.Dispatcher
import ru.g000sha256.reduktor.SideEffect

abstract class NewsSideEffect<A, S, T> : SideEffect<A, S> {

    val news: Flowable<T>
        get() = publishProcessor

    private val publishProcessor = PublishProcessor.create<T>()

    final override fun Dispatcher<A>.invoke(action: A, state: S) {
        val value = map(action, state) ?: return
        publishProcessor.onNext(value)
    }

    protected abstract fun map(action: A, state: S): T?

}