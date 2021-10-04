package ru.g000sha256.reduktor

import io.reactivex.Flowable

interface RxJavaReduktor<A, S> : Reduktor<A, S> {

    val states: Flowable<S>

}