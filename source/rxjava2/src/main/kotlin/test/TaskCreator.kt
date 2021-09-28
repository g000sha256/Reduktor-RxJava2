package test

import io.reactivex.Flowable
import io.reactivex.disposables.Disposable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

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

fun SideEffect.Context<String, String>.test() {
    taskCreator += TaskCreator.Creator1 {
        return@Creator1 Flowable
            .empty<String>()
            .subscribe()
    }
    taskCreator["1"] = TaskCreator.Creator1 {
        return@Creator1 Flowable
            .empty<String>()
            .subscribe()
    }
    taskCreator += TaskCreator.Creator2 { delay(100) }
    taskCreator["2"] = TaskCreator.Creator2 { launch {} }
}