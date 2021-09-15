package ru.g000sha256.reduktor

fun interface Reducer<A, S> {

    fun invoke(action: A, state: S): S

}