package g000sha256.reduktor.core

import java.util.LinkedList

class Store<A, S> internal constructor(
    private val logger: Logger,
    private val reducer: Reducer<A, S>,
    private val statesCallback: (S) -> Unit,
    private var state: S,
    initializersList: List<Initializer<A, S>>,
    sideEffectsList: List<SideEffect<A, S>>
) {

    private val any = Any()
    private val initializerEnvironmentsList: List<Pair<Initializer<A, S>, Environment<A>>>
    private val sideEffectEnvironmentsList: List<Pair<SideEffect<A, S>, Environment<A>>>

    private var isReleased = false
    private var counter = 0

    init {
        val actions = ActionsImpl()
        initializerEnvironmentsList = initializersList.map { it to EnvironmentImpl(actions) }
        sideEffectEnvironmentsList = sideEffectsList.map { it to EnvironmentImpl(actions) }
        logger.logTitle("--------INIT--------")
        logger.invoke("STATE  : $state")
        statesCallback(state)
        init(state)
    }

    fun release() {
        synchronized(any) {
            if (isReleased) return@synchronized
            isReleased = true
            initializerEnvironmentsList.forEach { it.second.tasks.clearAll() }
            sideEffectEnvironmentsList.forEach { it.second.tasks.clearAll() }
            logger.logTitle("------RELEASED------")
        }
    }

    private fun init(state: S) {
        synchronized(any) {
            initializerEnvironmentsList.forEach {
                if (isReleased) return@synchronized
                it.first.apply { it.second.invoke(state) }
            }
        }
    }

    private fun Logger.logTitle(title: String) {
        invoke(title)
        val thread = Thread.currentThread()
        invoke("THREAD : ${thread.name}")
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

        private fun handleAction(action: A) {
            val oldState = state
            logger.logTitle("-------ACTION-------")
            logger.invoke("ACTION > $action")
            logger.invoke("STATE  > $oldState")
            val newState = reducer.run { oldState.invoke(action) }
            if (newState == oldState) {
                logger.invoke("STATE  : NOT CHANGED")
            } else {
                state = newState
                logger.invoke("STATE  < $newState")
                statesCallback(newState)
            }
            sideEffectEnvironmentsList.forEach {
                if (isReleased) return
                it.first.apply { it.second.invoke(action, newState) }
            }
        }

    }

    private inner class EnvironmentImpl(override val actions: Actions<A>) : Environment<A> {

        override val tasks = TasksImpl()

        private inner class TasksImpl : Tasks {

            private val infoMutableList: MutableList<Info> = LinkedList()

            override fun add(task: Task) {
                synchronized(any) { add(task, key = null) }
            }

            override fun add(key: String, task: Task) {
                synchronized(any) { add(task, key) }
            }

            override fun clear(key: String) {
                synchronized(any) {
                    val info = infoMutableList.find { it.key == key } ?: return@synchronized
                    infoMutableList.remove(info)
                    logTaskRemoved(info)
                    info.task.cancel()
                }
            }

            override fun clearAll() {
                synchronized(any) {
                    if (infoMutableList.size == 0) return@synchronized
                    val infoList: List<Info> = ArrayList(infoMutableList)
                    infoMutableList.clear()
                    infoList.forEach {
                        logTaskRemoved(it)
                        it.task.cancel()
                    }
                }
            }

            private fun add(task: Task, key: String?) {
                if (isReleased) return
                checkContains(task)
                key?.also { clear(it) }
                val info = Info(task, id = ++counter, key = key)
                infoMutableList.add(info)
                logTaskAdded(info)
                task.start {
                    synchronized(any) {
                        val isRemoved = infoMutableList.remove(info)
                        if (isRemoved) logTaskRemoved(info)
                    }
                }
            }

            private fun checkContains(task: Task) {
                infoMutableList.find { it.task === task } ?: return
                throw IllegalArgumentException("Task has already been added")
            }

            private fun logTask(id: Int, key: String?) {
                logger.invoke("ID     : $id")
                key?.also { logger.invoke("KEY    : $it") }
            }

            private fun logTaskAdded(info: Info) {
                logger.logTitle("-----TASK ADDED-----")
                logTask(info.id, info.key)
            }

            private fun logTaskRemoved(info: Info) {
                logger.logTitle("----TASK REMOVED----")
                logTask(info.id, info.key)
            }

            private inner class Info(val task: Task, val id: Int, val key: String?)

        }

    }

}