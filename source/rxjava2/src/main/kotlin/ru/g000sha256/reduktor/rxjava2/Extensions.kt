package ru.g000sha256.reduktor.rxjava2

import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Maybe
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.functions.Action
import io.reactivex.functions.Consumer

infix fun <A> Actions<A>.post(action: A) {
    post(action)
}

infix fun <A> Actions<A>.post(actions: Array<A>) {
    post(*actions)
}

infix fun <A> Actions<A>.post(actions: Iterable<A>) {
    post(actions)
}

fun Completable.toTask(onError: Consumer<Throwable>? = null, onComplete: Action? = null): Task {
    return Task { onFinish ->
        return@Task subscribe(
            {
                onComplete?.run()
                onFinish.run()
            },
            {
                onError?.accept(it)
                onFinish.run()
            }
        )
    }
}

fun <A> Flowable<A>.toTask(
    onNext: Consumer<A>? = null,
    onError: Consumer<Throwable>? = null,
    onComplete: Action? = null
): Task {
    return Task { onFinish ->
        return@Task subscribe(
            { onNext?.accept(it) },
            {
                onError?.accept(it)
                onFinish.run()
            },
            {
                onComplete?.run()
                onFinish.run()
            }
        )
    }
}

fun <A> Maybe<A>.toTask(
    onSuccess: Consumer<A>? = null,
    onError: Consumer<Throwable>? = null,
    onComplete: Action? = null
): Task {
    return Task { onFinish ->
        return@Task subscribe(
            {
                onSuccess?.accept(it)
                onFinish.run()
            },
            {
                onError?.accept(it)
                onFinish.run()
            },
            {
                onComplete?.run()
                onFinish.run()
            }
        )
    }
}

fun <A> Observable<A>.toTask(
    onNext: Consumer<A>? = null,
    onError: Consumer<Throwable>? = null,
    onComplete: Action? = null
): Task {
    return Task { onFinish ->
        return@Task subscribe(
            { onNext?.accept(it) },
            {
                onError?.accept(it)
                onFinish.run()
            },
            {
                onComplete?.run()
                onFinish.run()
            }
        )
    }
}

fun <A> Single<A>.toTask(onSuccess: Consumer<A>? = null, onError: Consumer<Throwable>? = null): Task {
    return Task { onFinish ->
        return@Task subscribe(
            {
                onSuccess?.accept(it)
                onFinish.run()
            },
            {
                onError?.accept(it)
                onFinish.run()
            }
        )
    }
}