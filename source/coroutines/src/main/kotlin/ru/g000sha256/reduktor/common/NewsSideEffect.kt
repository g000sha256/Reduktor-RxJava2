package ru.g000sha256.reduktor.common

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import ru.g000sha256.reduktor.Dispatcher
import ru.g000sha256.reduktor.SideEffect

abstract class NewsSideEffect<A, S, T> : SideEffect<A, S> {

    val news: Flow<T>
        get() = mutableSharedFlow

    private val mutableSharedFlow = MutableSharedFlow<T>()

    final override fun Dispatcher<A>.invoke(action: A, state: S) {
        val result = map(action, state) ?: return
        launch { mutableSharedFlow.emit(result) }
    }

    protected abstract fun map(action: A, state: S): T?

}