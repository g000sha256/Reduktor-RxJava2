package ru.g000sha256.reduktor.coroutines

fun interface Logger {

    fun invoke(message: String)

}