package ru.g000sha256.reduktor

interface Sender<A> {

    fun send(action: A)

    fun send(vararg actions: A)

    fun send(actions: Iterable<A>)

}