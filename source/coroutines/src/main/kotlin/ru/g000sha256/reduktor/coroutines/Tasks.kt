package ru.g000sha256.reduktor.coroutines

class Tasks {

    private val lock = Any()

    private val tasksList = mutableListOf<Task>()
    private val tasksMap = mutableMapOf<String, Task>()

    private var isReleased = false

    fun add(task: Task) {
        synchronized(lock) {
            if (isReleased) return@synchronized
            checkContains(task)
            tasksList += task
            task.start { removeFromList(task) }
        }
    }

    fun add(key: String, task: Task) {
        synchronized(lock) {
            if (isReleased) return@synchronized
            checkContains(task)
            tasksMap[key] = task
            task.start { removeFromMap(task) }
        }
    }

    fun clear() {
        synchronized(lock) {
            if (tasksList.size > 0) {
                tasksList
                    .toList()
                    .apply { tasksList.clear() }
                    .forEach { it.cancel() }
            }
            if (tasksMap.size > 0) {
                tasksMap
                    .toMap()
                    .apply { tasksMap.clear() }
                    .forEach { it.value.cancel() }
            }
        }
    }

    fun clear(key: String) {
        synchronized(lock) {
            val task = tasksMap.remove(key) ?: return@synchronized
            task.cancel()
        }
    }

    fun release() {
        synchronized(lock) {
            isReleased = true
            clear()
        }
    }

    operator fun plusAssign(task: Task) {
        add(task)
    }

    operator fun set(key: String, task: Task) {
        add(key, task)
    }

    private fun checkContains(task: Task) {
        val contains = tasksList.contains(task) || tasksMap.containsValue(task)
        if (contains) throw IllegalArgumentException() // ToDo
    }

    private fun removeFromList(task: Task) {
        synchronized(lock) { tasksList.remove(task) }
    }

    private fun removeFromMap(task: Task) {
        synchronized(lock) {
            val entry = tasksMap.entries.find { it.value == task } ?: return@synchronized
            tasksMap.remove(entry.key)
        }
    }

}