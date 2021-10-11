package ru.g000sha256.reduktor.coroutines

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class Store<A, S>(
    coroutineScope: CoroutineScope,
    initialState: S,
    private val reducer: Reducer<A, S>,
    initializers: Iterable<Initializer<A, S>> = emptyList(),
    private val sideEffects: Iterable<SideEffect<A, S>> = emptyList(),
    private val logger: Logger = Logger {}
) {

    val states: Flow<S>

    private val any = Any()
    private val environmentsMutableList = mutableListOf<Environment<A>>()
    private val tasksMutableList = mutableListOf<Task>()
    private val tasksMutableMap = mutableMapOf<String, Task>()
    private val mutableStateFlow = MutableStateFlow(initialState)

    private var isReleased = false
    private var counter = 0
    private var state = initialState

    init {
        states = mutableStateFlow
        logger.invoke("-------INIT-------")
        logger.invoke("STATE  : $initialState")
        logThread()
        sideEffects.forEach {
            val environment = EnvironmentImpl(coroutineScope = coroutineScope)
            environmentsMutableList.add(environment)
        }
        initializers.forEach {
            val environment = EnvironmentImpl(coroutineScope = coroutineScope)
            environmentsMutableList.add(environment)
            it.apply { environment.invoke(initialState) }
        }
    }

    fun release() {
        synchronized(any) {
            isReleased = true
            environmentsMutableList.forEach { it.tasks.clearAll() }
            logger.invoke("-----RELEASED-----")
            logThread()
        }
    }

    private fun handleAction(action: A) {
        val oldState = state
        logger.invoke("------ACTION------")
        logger.invoke("ACTION > $action")
        logger.invoke("STATE  > $oldState")
        val newState = reducer.run { oldState.invoke(action) }
        if (newState == oldState) {
            logger.invoke("STATE  : NOT CHANGED")
            logThread()
        } else {
            state = newState
            logger.invoke("STATE  < $newState")
            logThread()
            mutableStateFlow.value = newState
        }
        sideEffects.forEachIndexed { index, sideEffect ->
            val environment = environmentsMutableList[index]
            sideEffect.apply { environment.invoke(action, newState) }
        }
    }

    private fun logTask(id: Int, key: String?) {
        logger.invoke("ID     : $id")
        key?.also { logger.invoke("KEY    : $it") }
        logThread()
    }

    private fun logTaskAdded(task: Task) {
        logger.invoke("----TASK ADDED----")
        logTask(task.id, task.key)
    }

    private fun logTaskRemoved(task: Task) {
        logger.invoke("---TASK REMOVED---")
        logTask(task.id, task.key)
    }

    private fun logThread() {
        val thread = Thread.currentThread()
        logger.invoke("THREAD : ${thread.name}")
    }

    private inner class ActionsImpl : Actions<A> {

        override fun post(action: A) {
            synchronized(any) {
                if (isReleased) return@synchronized
                handleAction(action)
            }

        }

        override fun post(vararg actions: A) {
            synchronized(any) {
                if (isReleased) return@synchronized
                actions.forEach(::handleAction)
            }
        }

        override fun post(actions: Iterable<A>) {
            synchronized(any) {
                if (isReleased) return@synchronized
                actions.forEach(::handleAction)
            }
        }

    }

    private inner class EnvironmentImpl(
        override val actions: Actions<A> = ActionsImpl(),
        override val coroutineScope: CoroutineScope,
        override val tasks: Tasks = TasksImpl()
    ) : Environment<A>

    private inner class TasksImpl : Tasks {

        override fun add(task: Task) {
            synchronized(any) {
                if (isReleased) return@synchronized
                checkContains(task)
                task.id = ++counter
                logTaskAdded(task)
                tasksMutableList += task
                task.start { synchronized(any) { removeFromList(task) } }
            }
        }

        override fun add(key: String, task: Task) {
            synchronized(any) {
                if (isReleased) return@synchronized
                checkContains(task)
                clear(key)
                task.id = ++counter
                task.key = key
                logTaskAdded(task)
                tasksMutableMap[key] = task
                task.start { synchronized(any) { removeFromMap(task) } }
            }
        }

        override fun clear(key: String) {
            synchronized(any) {
                val task = tasksMutableMap.remove(key) ?: return@synchronized
                logTaskRemoved(task)
                task.cancel()
            }
        }

        override fun clearAll() {
            synchronized(any) {
                if (tasksMutableList.size > 0) {
                    tasksMutableList
                        .toList()
                        .apply { tasksMutableList.clear() }
                        .forEach {
                            logTaskRemoved(it)
                            it.cancel()
                        }
                }
                if (tasksMutableMap.size > 0) {
                    tasksMutableMap
                        .toMap()
                        .apply { tasksMutableMap.clear() }
                        .forEach {
                            logTaskRemoved(it.value)
                            it.value.cancel()
                        }
                }
            }
        }

        override fun plusAssign(task: Task) {
            add(task)
        }

        override fun set(key: String, task: Task) {
            add(key, task)
        }

        private fun checkContains(task: Task) {
            val contains = tasksMutableList.contains(task) || tasksMutableMap.containsValue(task)
            if (contains) throw IllegalArgumentException() // ToDo
        }

        private fun removeFromList(task: Task) {
            logTaskRemoved(task)
            tasksMutableList.remove(task)
        }

        private fun removeFromMap(task: Task) {
            val entry = tasksMutableMap.entries.find { it.value == task } ?: return
            logTaskRemoved(task)
            tasksMutableMap.remove(entry.key)
        }

    }

}