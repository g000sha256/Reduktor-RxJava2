package test

import io.reactivex.disposables.Disposable

interface TaskCreator {

    fun create(key: String? = null, creator: Creator)

    fun interface Creator {

        fun invoke(): Disposable

    }

}