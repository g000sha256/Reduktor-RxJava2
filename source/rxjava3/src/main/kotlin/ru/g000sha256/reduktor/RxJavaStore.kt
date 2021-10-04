package ru.g000sha256.reduktor

import io.reactivex.rxjava3.core.Flowable

interface RxJavaStore<A, S> : Store<A> {

    val states: Flowable<S>

}