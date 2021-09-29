package ru.g000sha256.reduktor

import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Maybe
import io.reactivex.Observable
import io.reactivex.Single

fun interface Initializer<A, S> {

    fun Context<A>.invoke(initialState: S)

    interface Context<A> {

        val sender: Sender<A>
        val tasks: Tasks

        fun Completable.toTask(
            onComplete: () -> Unit = {},
            onError: (throwable: Throwable) -> Unit = {}
        ): Task

        fun <A> Flowable<A>.toTask(
            onNext: (action: A) -> Unit = {},
            onError: (throwable: Throwable) -> Unit = {},
            onComplete: () -> Unit = {}
        ): Task

        fun <A> Maybe<A>.toTask(
            onSuccess: (action: A) -> Unit = {},
            onError: (throwable: Throwable) -> Unit = {},
            onComplete: () -> Unit = {}
        ): Task

        fun <A> Observable<A>.toTask(
            onNext: (action: A) -> Unit = {},
            onError: (throwable: Throwable) -> Unit = {},
            onComplete: () -> Unit = {}
        ): Task

        fun <A> Single<A>.toTask(
            onSuccess: (action: A) -> Unit = {},
            onError: (throwable: Throwable) -> Unit = {}
        ): Task

        interface Tasks {

            operator fun plusAssign(task: Task)

            operator fun set(key: String, task: Task)

        }

    }

}