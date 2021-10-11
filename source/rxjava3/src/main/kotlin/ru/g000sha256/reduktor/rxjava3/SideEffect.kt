package ru.g000sha256.reduktor.rxjava3

fun interface SideEffect<A, S> {

    fun Environment<A>.invoke(action: A, state: S)

}