package ru.g000sha256.reduktor.core

interface Task {

    fun cancel()

    fun start(onComplete: (task: Task) -> Unit)

}