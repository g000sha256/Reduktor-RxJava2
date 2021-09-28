package test1

import test.Dispatcher
import test.StateAccessor
import test.TaskCleaner

fun interface SideEffect<A, S> {

    fun Context<A, S>.invoke(newAction: A)

    interface Context<A, S> {

        val dispatcher: Dispatcher<A>
        val stateAccessor: StateAccessor<S>
        val taskCleaner: TaskCleaner
        val taskCreator: TaskCreator

    }

}