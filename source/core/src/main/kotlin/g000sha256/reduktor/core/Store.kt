package g000sha256.reduktor.core

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

    private val thread: Thread
        get() = Thread.currentThread()

    private var isReleased = false
    private var counter = 0

    init {
        val actions = ActionsImpl()
        initializerEnvironmentsList = initializersList.toEnvironmentsList(actions)
        sideEffectEnvironmentsList = sideEffectsList.toEnvironmentsList(actions)
        val initialState = state
        logger.invoke("--------INIT--------")
        logger.invoke("THREAD : ${thread.name}")
        logger.invoke("STATE  : $initialState")
        statesCallback(initialState)
        // ToDo isReleased
        synchronized(any) { initializerEnvironmentsList.forEach { it.first.apply { it.second.invoke(initialState) } } }
    }

    fun release() {
        synchronized(any) {
            if (isReleased) return@synchronized
            isReleased = true
            initializerEnvironmentsList.forEach { it.second.tasks.clearAll() }
            sideEffectEnvironmentsList.forEach { it.second.tasks.clearAll() }
            logger.invoke("------RELEASED------")
            logger.invoke("THREAD : ${thread.name}")
        }
    }

    private fun <T> List<T>.toEnvironmentsList(actions: Actions<A>): List<Pair<T, Environment<A>>> {
        val environmentsList = ArrayList<Pair<T, Environment<A>>>()
        forEach { environmentsList += it to EnvironmentImpl(actions) }
        return environmentsList
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
            logger.invoke("-------ACTION-------")
            logger.invoke("THREAD : ${thread.name}")
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
            // ToDo isReleased
            sideEffectEnvironmentsList.forEach { it.first.apply { it.second.invoke(action, newState) } }
        }

    }

    private inner class EnvironmentImpl(override val actions: Actions<A>) : Environment<A> {

        override val tasks = TasksImpl()

    }

    private inner class TasksImpl : Tasks {

        private val infoMutableList: MutableList<Info> = ArrayList()

        override fun add(task: Task) {
            synchronized(any) {
                if (isReleased) return@synchronized
                checkContains(task)
                add(task, key = null)
            }
        }

        override fun add(key: String, task: Task) {
            synchronized(any) {
                if (isReleased) return@synchronized
                checkContains(task)
                clear(key)
                add(task, key)
            }
        }

        override fun clear(key: String) {
            synchronized(any) {
                val info = infoMutableList.find { it.key == key } ?: return@synchronized
                infoMutableList.remove(info)
                clear(info)
            }
        }

        override fun clearAll() {
            synchronized(any) {
                if (infoMutableList.size == 0) return@synchronized
                val infoArrayList = ArrayList(infoMutableList)
                infoMutableList.clear()
                infoArrayList.forEach(::clear)
            }
        }

        private fun add(task: Task, key: String?) {
            val info = Info(task, id = ++counter, key = key)
            infoMutableList += info
            logTaskAdded(info.id, info.key)
            task.start { synchronized(any) { remove(task) } }
        }

        private fun checkContains(task: Task) {
            val info = infoMutableList.find { it.task === task }
            info ?: return
            throw IllegalArgumentException("Task has already been added")
        }

        private fun clear(info: Info) {
            logTaskRemoved(info.id, info.key)
            info.task.cancel()
        }

        private fun logTaskAdded(id: Int, key: String?) {
            logger.invoke("-----TASK ADDED-----")
            logger.invoke("THREAD : ${thread.name}")
            logger.invoke("ID     : $id")
            key?.also { logger.invoke("KEY    : $it") }
        }

        private fun logTaskRemoved(id: Int, key: String?) {
            logger.invoke("----TASK REMOVED----")
            logger.invoke("THREAD : ${thread.name}")
            logger.invoke("ID     : $id")
            key?.also { logger.invoke("KEY    : $it") }
        }

        private fun remove(task: Task) {
            val info = infoMutableList.find { it.task === task } ?: return
            infoMutableList.remove(info)
            clear(info)
        }

        private inner class Info(val task: Task, val id: Int, val key: String?)

    }

}