package ru.g000sha256.reduktor

internal fun Logger.logSeparator() {
    invoke("---------")
}

internal fun Logger.logThread() {
    val thread = Thread.currentThread()
    invoke("THREAD : ${thread.name}")
}