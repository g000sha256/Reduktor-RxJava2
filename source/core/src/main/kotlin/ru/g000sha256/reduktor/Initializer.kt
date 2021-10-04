package ru.g000sha256.reduktor

fun interface Initializer<A, S> {

    fun Actions<A>.invoke(initialState: S)

}