package ru.g000sha256.reduktor

import kotlin.coroutines.CoroutineContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

fun CoroutineScope.task(block: suspend CoroutineScope.() -> Unit): Task {
    val coroutineContext = coroutineContext
    val job = launch(Dispatchers.Unconfined, CoroutineStart.LAZY) { launch(coroutineContext, block = block) }
}

fun CoroutineScope.task(context: CoroutineContext, block: suspend CoroutineScope.() -> Unit): Task {
    val coroutineContext = coroutineContext
    val job = launch(context, CoroutineStart.LAZY, block)
}