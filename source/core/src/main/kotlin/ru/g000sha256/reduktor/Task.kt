package ru.g000sha256.reduktor

interface Task {

    fun cancel()

    fun start(onComplete: () -> Unit)

}