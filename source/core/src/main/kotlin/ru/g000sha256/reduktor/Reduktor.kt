package ru.g000sha256.reduktor

interface Reduktor<A, S> {

    fun dispatch(action: A)

    fun start()

    fun stop()

}