package test

import io.reactivex.disposables.Disposable

interface TaskCreator {

    fun create(creator: Creator)

    fun create(key: String, creator: Creator)

    fun interface Creator {

        fun invoke(): Disposable

    }

}