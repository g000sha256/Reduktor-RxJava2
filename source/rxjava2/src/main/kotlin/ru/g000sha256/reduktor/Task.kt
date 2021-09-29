package ru.g000sha256.reduktor

abstract class Task {

    internal abstract val onComplete: () -> Unit

    internal abstract fun cancel()

    internal abstract fun start()

}