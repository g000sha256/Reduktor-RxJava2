package test

fun interface Reducer<A, S> {

    fun invoke(newAction: A, currentState: S): S

}