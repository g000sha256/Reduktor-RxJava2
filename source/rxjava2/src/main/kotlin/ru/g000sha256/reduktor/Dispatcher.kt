package ru.g000sha256.reduktor

import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Maybe
import io.reactivex.Observable
import io.reactivex.Single

interface Dispatcher<A> {

    fun cancel(key: String)

    fun dispatch(action: A)

    fun Completable.launch(key: String? = null)

    fun Flowable<A>.launch(key: String? = null)

    fun Maybe<A>.launch(key: String? = null)

    fun Observable<A>.launch(key: String? = null)

    fun Single<A>.launch(key: String? = null)

}