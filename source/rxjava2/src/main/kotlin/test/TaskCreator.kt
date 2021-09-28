package test

import io.reactivex.disposables.Disposable
import kotlinx.coroutines.CoroutineScope

interface TaskCreator {

    operator fun plusAssign(creator: Creator1)

    operator fun plusAssign(creator: Creator2)

    operator fun set(key: String, creator: Creator1)

    operator fun set(key: String, creator: Creator2)

    fun interface Creator1 {

        fun invoke(): Disposable

    }

    fun interface Creator2 {

        suspend fun CoroutineScope.invoke()

    }

}