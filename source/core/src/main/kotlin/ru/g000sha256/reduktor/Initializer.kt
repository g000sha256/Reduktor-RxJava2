package ru.g000sha256.reduktor

fun interface Initializer<A, S> {

    fun Context<A>.invoke(initialState: S)

    interface Context<A> {

        val sender: Sender<A>
        val tasks: Tasks

        interface Tasks {

            operator fun plusAssign(task: Task)

            operator fun set(key: String, task: Task)

        }

    }

}