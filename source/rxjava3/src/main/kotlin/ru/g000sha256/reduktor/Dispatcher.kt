package ru.g000sha256.reduktor

import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Maybe
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single

interface Dispatcher<A> {

    fun cancel(key: String)

    fun dispatch(action: A)

    fun Completable.launch(key: String? = null)

    fun Flowable<A>.launch(key: String? = null)

    fun Maybe<A>.launch(key: String? = null)

    fun Observable<A>.launch(key: String? = null)

    fun Single<A>.launch(key: String? = null)

}