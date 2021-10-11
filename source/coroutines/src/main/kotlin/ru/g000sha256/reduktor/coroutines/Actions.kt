package ru.g000sha256.reduktor.coroutines

interface Actions<A> {

    fun post(action: A)

    fun post(vararg actions: A)

    fun post(actions: Iterable<A>)

}