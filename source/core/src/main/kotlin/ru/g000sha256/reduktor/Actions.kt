package ru.g000sha256.reduktor

fun interface Actions<A> {

    fun post(action: A)

}