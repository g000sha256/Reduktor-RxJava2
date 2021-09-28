package test1

import io.reactivex.Flowable
import test.SideEffect
import test.Task
import test.TaskCreator

fun SideEffect.Context<String, String>.test() {
    taskCreator += Flowable.empty<String>()
    taskCreator["1"] = Flowable.empty<String>()
}

operator fun <A> TaskCreator.plusAssign(flowable: Flowable<A>) {
    this += {
        val disposable = flowable.subscribe()
        object : Task {

            override val isCompleted: Boolean
                get() = disposable.isDisposed

            override fun cancel() {
                disposable.dispose()
            }

        }
    }
}

operator fun <A> TaskCreator.set(key: String, flowable: Flowable<A>) {
    this[key] = {
        val disposable = flowable.subscribe()
        object : Task {

            override val isCompleted: Boolean
                get() = disposable.isDisposed

            override fun cancel() {
                disposable.dispose()
            }

        }
    }
}