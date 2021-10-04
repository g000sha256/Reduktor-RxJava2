package ru.g000sha256.reduktor.actions

interface Actions<A> {

    fun post(action: A)

    fun post(vararg actions: A)

    fun post(actions: Iterable<A>)

}