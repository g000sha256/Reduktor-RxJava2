package test

interface Dispatcher<A> {

    fun dispatch(action: A)

}