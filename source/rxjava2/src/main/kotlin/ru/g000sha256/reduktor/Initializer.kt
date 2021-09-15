package ru.g000sha256.reduktor

fun interface Initializer<A, S> {

    fun Dispatcher<A>.invoke(initialState: S)

}