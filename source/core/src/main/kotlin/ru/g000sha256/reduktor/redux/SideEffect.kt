package ru.g000sha256.reduktor.redux

fun interface SideEffect<A, S> {

    fun Actions<A>.invoke(action: A, state: S)

}