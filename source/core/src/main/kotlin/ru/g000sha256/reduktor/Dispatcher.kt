package ru.g000sha256.reduktor

fun interface Dispatcher<V> {

    fun dispatch(value: V)

}