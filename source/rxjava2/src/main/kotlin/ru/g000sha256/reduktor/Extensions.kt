package ru.g000sha256.reduktor

import io.reactivex.Flowable
import io.reactivex.disposables.Disposable

fun <A> Flowable<A>.toTask(
    onNext: (action: A) -> Unit = {},
    onError: (throwable: Throwable) -> Unit = {},
    onComplete: () -> Unit = {}
): Task {
    val flowable = this
    return object : Task {

        override val isCompleted: Boolean
            get() = _isCompleted

        private val lock = Any()

        private var _isCompleted = false
        private var disposable: Disposable? = null

        override fun cancel() {
            synchronized(lock) {
                disposable?.dispose()
                disposable = null
            }
        }

        override fun start() {
            synchronized(lock) {
                if (disposable == null) {
                    disposable = flowable
                        .doOnCancel { }
                        .doOnComplete { }
                        .doOnError { }
                        .subscribe(onNext, onError, onComplete)
                }
            }
        }

    }
}