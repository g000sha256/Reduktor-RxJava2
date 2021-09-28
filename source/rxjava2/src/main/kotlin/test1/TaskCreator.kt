package test1

import io.reactivex.disposables.Disposable

interface TaskCreator {

    operator fun plusAssign(creator: Creator)

    operator fun set(key: String, creator: Creator)

    fun interface Creator {

        fun invoke(): Disposable

    }

}