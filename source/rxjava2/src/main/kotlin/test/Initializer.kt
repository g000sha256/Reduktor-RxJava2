package test

fun interface Initializer<A, S> {

    fun Context<A>.invoke(initialState: S)

    interface Context<A> {

        val actions: Actions<A>
        val tasks: Tasks

        interface Tasks {

            operator fun plusAssign(task: Task)

            operator fun set(key: String, task: Task)

        }

    }

}