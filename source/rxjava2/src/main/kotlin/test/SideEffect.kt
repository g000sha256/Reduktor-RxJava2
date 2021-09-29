package test

fun interface SideEffect<A, S> {

    fun Context<A, S>.invoke(newAction: A)

    interface Context<A, S> : Dispatcher<A>, StateAccessor<S>, TaskCleaner, TaskCreator

}