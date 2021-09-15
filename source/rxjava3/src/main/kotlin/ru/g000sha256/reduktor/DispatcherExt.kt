package ru.g000sha256.reduktor

import io.reactivex.rxjava3.processors.FlowableProcessor

fun <A> Dispatcher(actions: FlowableProcessor<A>, disposables: Disposables): Dispatcher<A> {
    return DispatcherImpl(disposables, actions)
}