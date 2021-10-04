package ru.g000sha256.reduktor.redux

fun interface Reducer<A, S> {

    fun S.invoke(action: A): S

}