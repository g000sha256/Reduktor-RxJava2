package test

fun interface Initializer<A, S> {

    fun Context<A>.invoke(initialState: S)

    interface Context<A> {

        val actions: Actions<A>
        val jobs: Jobs

        interface Jobs : TaskCreator

    }

}