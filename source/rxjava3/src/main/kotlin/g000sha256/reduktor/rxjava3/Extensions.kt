package g000sha256.reduktor.rxjava3

import g000sha256.reduktor.core.Task
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Maybe
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.functions.Action
import io.reactivex.rxjava3.functions.Consumer

fun Completable.toTask(onError: Consumer<Throwable>? = null, onComplete: Action? = null): Task {
    return createTask { onFinish ->
        return@createTask subscribe(
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
    return createTask { onFinish ->
        return@createTask subscribe(
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
    return createTask { onFinish ->
        return@createTask subscribe(
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
    return createTask { onFinish ->
        return@createTask subscribe(
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
    return createTask { onFinish ->
        return@createTask subscribe(
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

private fun createTask(creator: (Action) -> Disposable): Task {
    return TaskImpl(creator)
}

private class TaskImpl(private val creator: (Action) -> Disposable) : Task {

    private val any = Any()

    private var isDisposed = false
    private var disposable: Disposable? = null

    override fun cancel() {
        synchronized(any) {
            if (isDisposed) return@synchronized
            isDisposed = true
            dispose()
        }
    }

    override fun start(onComplete: () -> Unit) {
        synchronized(any) {
            if (disposable != null) return@synchronized
            if (isDisposed) return@synchronized
            disposable = creator(onComplete)
            if (isDisposed) dispose()
        }
    }

    private fun dispose() {
        disposable?.dispose()
        disposable = null
    }

}