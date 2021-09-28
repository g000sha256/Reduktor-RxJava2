package test1

import io.reactivex.Flowable

fun SideEffect.Context<String, String>.test() {
    taskCreator += TaskCreator.Creator {
        return@Creator Flowable
            .empty<String>()
            .subscribe()
    }
    taskCreator["1"] = TaskCreator.Creator {
        return@Creator Flowable
            .empty<String>()
            .subscribe()
    }
}