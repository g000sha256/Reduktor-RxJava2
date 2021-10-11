package ru.g000sha256.reduktor.core

fun interface Initializer<A, S> {

    fun ActionsOwner<A>.invoke(initialState: S)

}