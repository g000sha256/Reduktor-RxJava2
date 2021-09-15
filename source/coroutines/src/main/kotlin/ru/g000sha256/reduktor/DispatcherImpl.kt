package ru.g000sha256.reduktor

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.launch

internal class DispatcherImpl<A>(
    private val coroutineScope: CoroutineScope,
    private val flowCollector: FlowCollector<A>,
    private val jobs: Jobs
) : Dispatcher<A> {

    private val lock = Any()

    override fun cancel(key: String) {
        synchronized(lock) { jobs.remove(key) }
    }

    override fun cancel(vararg keys: String) {
        synchronized(lock) { keys.forEach { jobs.remove(it) } }
    }

    override fun dispatch(action: A) {
        coroutineScope.launch(ImmediateCoroutineDispatcher) { flowCollector.emit(action) }
    }

    override fun dispatch(vararg actions: A) {
        coroutineScope.launch(ImmediateCoroutineDispatcher) { actions.forEach { flowCollector.emit(it) } }
    }

    override fun dispatch(actions: Iterable<A>) {
        coroutineScope.launch(ImmediateCoroutineDispatcher) { actions.forEach { flowCollector.emit(it) } }
    }

    override fun launch(key: String?, callback: suspend CoroutineScope.() -> Unit) {
        synchronized(lock) {
            val job = coroutineScope.launch(ImmediateCoroutineDispatcher, CoroutineStart.LAZY, callback)
            if (key == null) {
                job.invokeOnCompletion { jobs -= job }
                jobs += job
            } else {
                jobs.remove(key)
                job.invokeOnCompletion { jobs.remove(key) }
                jobs[key] = job
            }
            job.start()
        }
    }

}