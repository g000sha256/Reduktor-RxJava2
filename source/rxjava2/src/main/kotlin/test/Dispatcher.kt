package test

interface Dispatcher<A> {

    fun dispatch(action: A)

    fun dispatch(vararg actions: A)

    fun dispatch(actions: Iterable<A>)

}