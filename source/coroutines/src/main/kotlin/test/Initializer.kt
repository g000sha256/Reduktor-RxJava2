package test

import kotlinx.coroutines.CoroutineScope

fun interface Initializer<A, S> {

    fun Context<A>.invoke(initialState: S)

    interface Context<A> {

        val actions: Actions<A>
        val tasks: Tasks

        fun task(callback: suspend CoroutineScope.() -> Unit): Task

        interface Tasks {

            operator fun plusAssign(task: Task)

            operator fun set(key: String, task: Task)

        }

    }

}