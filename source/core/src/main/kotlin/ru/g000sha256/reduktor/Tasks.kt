package ru.g000sha256.reduktor

import java.util.UUID

class Tasks(
    private val logger: Logger = Logger {}
) {

    private val lock = Any()
    private val mutableMap = mutableMapOf<String, Task>()

    fun add(task: Task) {
        val key = UUID
            .randomUUID()
            .toString()
        add(key, task)
    }

    fun add(key: String, task: Task) {
        synchronized(lock) {
            val containsValue = mutableMap.containsValue(task)
            if (containsValue) throw IllegalArgumentException() // ToDo
            remove(key)
            logAdd(key)
            mutableMap[key] = task
        }
    }

    fun clear() {
        synchronized(lock) {
            mutableMap.forEach {
                logRemove(it.key)
                it.value.cancel()
            }
            mutableMap.clear()
        }
    }

    fun remove(key: String) {
        synchronized(lock) {
            val task = mutableMap.remove(key) ?: return@synchronized
            logRemove(key)
            task.cancel()
        }
    }

    operator fun plusAssign(task: Task) {
        add(task)
    }

    operator fun set(key: String, task: Task) {
        add(key, task)
    }

    private fun logAdd(key: String) {
        logger.apply {
            logSeparator()
            invoke("TASK   + $key")
            logThread()
        }
    }

    private fun logRemove(key: String) {
        logger.apply {
            logSeparator()
            invoke("TASK   - $key")
            logThread()
        }
    }

}