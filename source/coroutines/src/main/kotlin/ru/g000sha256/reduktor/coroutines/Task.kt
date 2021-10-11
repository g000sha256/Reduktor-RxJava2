package ru.g000sha256.reduktor.coroutines

import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job

class Task internal constructor(private val job: Job) {

    internal var id = 0
    internal var key: String? = null

    internal fun cancel() {
        val internalCancellationException = InternalCancellationException()
        job.cancel(internalCancellationException)
    }

    internal fun start(onComplete: (Task) -> Unit) {
        job.invokeOnCompletion { if (it !is InternalCancellationException) onComplete(this) }
        job.start()
    }

    private class InternalCancellationException : CancellationException()

}