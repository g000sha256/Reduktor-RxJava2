package test

fun interface Reducer<A, S> {

    fun S.invoke(action: A): S

}