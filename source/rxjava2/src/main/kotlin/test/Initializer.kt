package test

fun interface Initializer<A, S> {

    fun Context<A>.invoke(initialState: S)

    interface Context<A> : Dispatcher<A>, TaskCreator

}