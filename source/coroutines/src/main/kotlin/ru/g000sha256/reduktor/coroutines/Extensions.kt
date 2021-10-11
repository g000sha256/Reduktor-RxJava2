package ru.g000sha256.reduktor.coroutines

import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.launch

infix fun <A> Actions<A>.post(action: A) {
    post(action)
}

infix fun <A> Actions<A>.post(actions: Array<A>) {
    post(*actions)
}

infix fun <A> Actions<A>.post(actions: Iterable<A>) {
    post(actions)
}

fun CoroutineScope.createTask(context: CoroutineContext? = null, block: suspend CoroutineScope.() -> Unit): Task {
    val coroutineContext = context ?: EmptyCoroutineContext
    val job = launch(coroutineContext, CoroutineStart.LAZY, block)
    return Task(job)
}