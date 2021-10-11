package ru.g000sha256.reduktor.coroutines

interface Tasks {

    fun add(task: Task)

    fun add(key: String, task: Task)

    fun clear(key: String)

    fun clearAll()

    operator fun plusAssign(task: Task)

    operator fun set(key: String, task: Task)

}