package test

fun interface Dispatcher<A> {

    fun dispatch(action: A)

}