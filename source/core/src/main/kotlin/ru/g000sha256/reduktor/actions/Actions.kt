package ru.g000sha256.reduktor.actions

fun interface Actions<A> {

    fun post(action: A)

}