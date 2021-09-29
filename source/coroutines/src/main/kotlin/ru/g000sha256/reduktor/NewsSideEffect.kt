package ru.g000sha256.reduktor

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow

abstract class NewsSideEffect<A, S, T>(private val coroutineScope: CoroutineScope) : SideEffect<A, S> {

    val news: Flow<T>

    private val mutableSharedFlow = MutableSharedFlow<T>()

    init {
        news = mutableSharedFlow
    }

    final override fun SideEffect.Context<A>.invoke(action: A, state: S) {
        val result = map(action, state) ?: return
        tasks += coroutineScope.task(Dispatchers.Unconfined) { mutableSharedFlow.emit(result) }
    }

    protected abstract fun map(action: A, state: S): T?

}