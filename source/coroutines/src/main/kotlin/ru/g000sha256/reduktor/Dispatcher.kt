package ru.g000sha256.reduktor

import kotlinx.coroutines.CoroutineScope

interface Dispatcher<A> {

    fun cancel(key: String)

    fun cancel(vararg keys: String)

    fun dispatch(action: A)

    fun dispatch(vararg actions: A)

    fun dispatch(actions: Iterable<A>)

    fun launch(key: String? = null, callback: suspend CoroutineScope.() -> Unit)

}