package ru.g000sha256.reduktor

interface Reduktor<A> {

    fun dispatch(action: A)

}