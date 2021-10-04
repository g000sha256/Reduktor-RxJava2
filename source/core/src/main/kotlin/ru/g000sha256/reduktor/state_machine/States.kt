package ru.g000sha256.reduktor.state_machine

interface States<S> {

    fun post(state: S)

}