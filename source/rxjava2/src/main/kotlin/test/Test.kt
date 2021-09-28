package test

import io.reactivex.Flowable
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

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