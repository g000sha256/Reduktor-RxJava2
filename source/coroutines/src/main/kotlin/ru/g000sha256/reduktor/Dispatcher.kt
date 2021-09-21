package ru.g000sha256.reduktor

import kotlin.coroutines.CoroutineContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow

interface Dispatcher<A> {

    fun cancel(key: String)

    fun dispatch(action: A)

    fun launch(context: CoroutineContext? = null, key: String? = null, callback: suspend CoroutineScope.() -> Unit)

    fun Flow<A>.launch(context: CoroutineContext? = null, key: String? = null)

}