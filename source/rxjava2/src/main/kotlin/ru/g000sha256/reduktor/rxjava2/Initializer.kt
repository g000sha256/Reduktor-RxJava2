package ru.g000sha256.reduktor.rxjava2

fun interface Initializer<A, S> {

    fun Environment<A>.invoke(initialState: S)

}