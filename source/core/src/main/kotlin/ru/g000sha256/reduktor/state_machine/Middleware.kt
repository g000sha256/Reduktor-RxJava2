package ru.g000sha256.reduktor.state_machine

fun interface Middleware<A, S> {

    fun States<S>.invoke(action: A, state: S)

}