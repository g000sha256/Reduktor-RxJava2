package ru.g000sha256.reduktor.states

fun interface Middleware<A, S> {

    fun States<S>.invoke(action: A, state: S)

}