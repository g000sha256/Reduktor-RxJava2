package ru.g000sha256.reduktor

import kotlinx.coroutines.CoroutineScope

fun interface SideEffect<A, S> {

    fun Context<A>.invoke(action: A, state: S)

    interface Context<A> {

        val sender: Sender<A>
        val tasks: Tasks

        fun unconfinedTask(block: suspend CoroutineScope.() -> Unit): Task

        interface Tasks {

            fun cancel(key: String)

            operator fun plusAssign(task: Task)

            operator fun set(key: String, task: Task)

        }

    }

}