package ru.g000sha256.reduktor.rxjava2

import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Maybe
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Action
import io.reactivex.functions.Consumer
import ru.g000sha256.reduktor.core.Task

fun Completable.toTask(onError: Consumer<Throwable>? = null, onComplete: Action? = null): Task {
    return TaskImpl { onFinish ->
        return@TaskImpl subscribe(
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
    return TaskImpl { onFinish ->
        return@TaskImpl subscribe(
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
    return TaskImpl { onFinish ->
        return@TaskImpl subscribe(
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
    return TaskImpl { onFinish ->
        return@TaskImpl subscribe(
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
    return TaskImpl { onFinish ->
        return@TaskImpl subscribe(
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

// ToDo
private class TaskImpl(private val creator: (Action) -> Disposable) : Task {

    private var isDisposed = false
    private var disposable: Disposable? = null

    override fun cancel() {
        isDisposed = true
        dispose()
    }

    override fun start(onComplete: (task: Task) -> Unit) {
        disposable = creator { onComplete(this) }
        if (isDisposed) dispose()
    }

    private fun dispose() {
        disposable?.dispose()
        disposable = null
    }

}