package ru.g000sha256.reduktor

fun interface SideEffect<A, S> {

    fun Dispatcher<A>.invoke(action: A, state: S)

}