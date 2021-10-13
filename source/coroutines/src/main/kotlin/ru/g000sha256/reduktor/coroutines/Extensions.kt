package ru.g000sha256.reduktor.coroutines

import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.launch
import ru.g000sha256.reduktor.core.Task

// ToDo
fun CoroutineScope.createTask(context: CoroutineContext? = null, block: suspend CoroutineScope.() -> Unit): Task {
    val coroutineContext = context ?: EmptyCoroutineContext
    val job = launch(coroutineContext, CoroutineStart.LAZY, block)
    return object : Task {

        override fun cancel() {
            val internalCancellationException = InternalCancellationException()
            job.cancel(internalCancellationException)
        }

        override fun start(onComplete: (task: Task) -> Unit) {
            job.invokeOnCompletion { if (it !is InternalCancellationException) onComplete(this) }
            job.start()
        }

    }
}

private class InternalCancellationException : CancellationException()