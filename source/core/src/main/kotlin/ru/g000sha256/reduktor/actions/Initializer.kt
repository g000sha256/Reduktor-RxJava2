package ru.g000sha256.reduktor.actions

import ru.g000sha256.reduktor.Dispatcher

fun interface Initializer<A, S> {

    fun Dispatcher<A>.invoke(initialState: S)

}