package ru.g000sha256.reduktor.states

import ru.g000sha256.reduktor.Dispatcher

fun interface Middleware<A, S> {

    fun Dispatcher<S>.invoke(action: A, state: S)

}