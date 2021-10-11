package ru.g000sha256.reduktor.rxjava2

fun interface Reducer<A, S> {

    fun S.invoke(action: A): S

}