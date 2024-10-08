package g000sha256.reduktor.coroutines

import g000sha256.reduktor.core.Task
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.launch

fun CoroutineScope.createTask(context: CoroutineContext? = null, block: suspend CoroutineScope.() -> Unit): Task {
    val coroutineContext = context ?: EmptyCoroutineContext
    val job = launch(coroutineContext, CoroutineStart.LAZY, block)
    return object : Task {

        private val any = Any()

        override fun cancel() {
            synchronized(any) {
                if (job.isActive) {
                    val internalCancellationException = InternalCancellationException()
                    job.cancel(internalCancellationException)
                }
            }
        }

        override fun start(onComplete: () -> Unit) {
            synchronized(any) {
                if (job.isActive) return@synchronized
                job.invokeOnCompletion { if (it !is InternalCancellationException) onComplete() }
                job.start()
            }
        }

    }
}

private class InternalCancellationException : CancellationException()