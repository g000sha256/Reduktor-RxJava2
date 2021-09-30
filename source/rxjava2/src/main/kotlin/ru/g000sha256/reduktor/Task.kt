package ru.g000sha256.reduktor

abstract class Task {

    abstract fun test(): Test

    internal abstract val onComplete: () -> Unit

    internal abstract fun cancel()

    internal abstract fun start()

    interface Test {

        val isTerminated: Boolean

        fun cancel()

        fun start()

    }

}