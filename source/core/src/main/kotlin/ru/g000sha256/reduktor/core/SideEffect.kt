package ru.g000sha256.reduktor.core

fun interface SideEffect<A, S> {

    fun Environment<A>.invoke(action: A, state: S)

}