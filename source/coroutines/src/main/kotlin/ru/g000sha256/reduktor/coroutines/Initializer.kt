package ru.g000sha256.reduktor.coroutines

fun interface Initializer<A, S> {

    fun Environment<A>.invoke(initialState: S)

}