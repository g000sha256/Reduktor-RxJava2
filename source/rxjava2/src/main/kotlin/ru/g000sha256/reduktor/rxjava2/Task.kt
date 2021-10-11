package ru.g000sha256.reduktor.rxjava2

import io.reactivex.disposables.Disposable
import io.reactivex.functions.Action

class Task internal constructor(private val creator: (Action) -> Disposable) {

    internal var id = 0
    internal var key: String? = null

    private var isDisposed = false
    private var disposable: Disposable? = null

    internal fun cancel() {
        isDisposed = true
        dispose()
    }

    internal fun start(onComplete: (Task) -> Unit) {
        disposable = creator { onComplete(this) }
        if (isDisposed) dispose()
    }

    private fun dispose() {
        disposable?.dispose()
        disposable = null
    }

}