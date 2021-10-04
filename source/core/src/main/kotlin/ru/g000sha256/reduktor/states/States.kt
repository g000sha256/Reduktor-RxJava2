package ru.g000sha256.reduktor.states

interface States<S> {

    fun post(state: S)

}