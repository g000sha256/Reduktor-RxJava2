package ru.g000sha256.reduktor

interface Task {

    val isCompleted: Boolean

    fun cancel()

    fun start()

}