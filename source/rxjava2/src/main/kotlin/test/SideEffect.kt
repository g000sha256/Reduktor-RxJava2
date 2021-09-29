package test

fun interface SideEffect<A, S> {

    fun Context<A, S>.invoke(newAction: A)

    interface Context<A, S> : StateAccessor<S> {

        val actions: Actions<A>
        val jobs: Jobs

        interface Jobs : TaskCleaner, TaskCreator

    }

}