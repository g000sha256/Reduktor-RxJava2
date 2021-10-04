package ru.g000sha256.reduktor.actions

import ru.g000sha256.reduktor.Dispatcher

fun interface SideEffect<A, S> {

    fun Dispatcher<A>.invoke(action: A, state: S)

}