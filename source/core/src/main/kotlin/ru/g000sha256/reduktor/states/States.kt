package ru.g000sha256.reduktor.states

fun interface States<S> {

    fun post(state: S)

}