package ru.g000sha256.reduktor

fun interface Initializer<A, S> {

    fun ActionsOwner<A>.invoke(state: S)

}