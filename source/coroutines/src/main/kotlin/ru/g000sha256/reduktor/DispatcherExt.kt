package ru.g000sha256.reduktor

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.FlowCollector

fun <A> Dispatcher(scope: CoroutineScope, actions: FlowCollector<A>, jobs: Jobs): Dispatcher<A> {
    return DispatcherImpl(scope, actions, jobs)
}