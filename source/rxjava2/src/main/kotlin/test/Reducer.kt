package test

fun interface Reducer<A, S> {

    fun reduce(action: A, currentState: S): S

}