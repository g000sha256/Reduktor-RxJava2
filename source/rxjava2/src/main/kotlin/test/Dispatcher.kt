package test

interface Dispatcher<A> {

    fun send(action: A)

    fun send(vararg actions: A)

    fun send(actions: Iterable<A>)

}