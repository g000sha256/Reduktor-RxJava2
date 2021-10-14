package g000sha256.reduktor.core

class Store<A, S>(
    initialState: S,
    private val reducer: Reducer<A, S>,
    initializers: Collection<Initializer<A, S>> = ArrayList(),
    sideEffects: Collection<SideEffect<A, S>> = ArrayList(),
    private val logger: Logger = Logger {},
    private val statesCallback: (state: S) -> Unit
) {

    private val any = Any()
    private val initializersList: List<Initializer<A, S>> = ArrayList(initializers)
    private val sideEffectsList: List<SideEffect<A, S>> = ArrayList(sideEffects)
    private val environmentsMutableList: MutableList<Environment<A>> = ArrayList(initializers.size + sideEffects.size)

    private val thread: Thread
        get() = Thread.currentThread()

    private var isReleased = false
    private var counter = 0
    private var state = initialState

    init {
        logger.invoke("--------INIT--------")
        logger.invoke("THREAD : ${thread.name}")
        logger.invoke("STATE  : $initialState")
        statesCallback(initialState)
        val actions = createActions()
        sideEffectsList.forEach { _ -> environmentsMutableList += createEnvironment(actions) }
        synchronized(any) {
            initializersList.forEach {
                if (isReleased) return@forEach // ToDo
                val environment = createEnvironment(actions)
                environmentsMutableList += environment
                it.apply { environment.invoke(initialState) }
            }
        }
    }

    fun release() {
        synchronized(any) {
            if (isReleased) return@synchronized
            isReleased = true
            environmentsMutableList.forEach { it.tasks.clearAll() }
            logger.invoke("------RELEASED------")
            logger.invoke("THREAD : ${thread.name}")
        }
    }

    private fun createActions(): Actions<A> {
        return object : Actions<A> {

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
                logger.invoke("-------ACTION-------")
                logger.invoke("THREAD : ${thread.name}")
                logger.invoke("ACTION > $action")
                val oldState = state
                logger.invoke("STATE  > $oldState")
                val newState = reducer.run { oldState.invoke(action) }
                if (newState == oldState) {
                    logger.invoke("STATE  : NOT CHANGED")
                } else {
                    state = newState
                    logger.invoke("STATE  < $newState")
                    statesCallback(newState)
                }
                sideEffectsList.forEachIndexed { index, sideEffect ->
                    val environment = environmentsMutableList[index]
                    sideEffect.apply { environment.invoke(action, newState) }
                }
            }

        }
    }

    private fun createEnvironment(actions: Actions<A>): Environment<A> {
        return object : Environment<A> {

            override val actions = actions
            override val tasks = createTasks()

        }
    }

    private fun createTasks(): Tasks {
        return object : Tasks {

            private val taskInfoArrayList = ArrayList<TaskInfo>()

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
                    val taskInfo = taskInfoArrayList.find { it.key == key } ?: return@synchronized
                    taskInfoArrayList.remove(taskInfo)
                    clear(taskInfo)
                }
            }

            override fun clearAll() {
                synchronized(any) {
                    if (taskInfoArrayList.size == 0) return@synchronized
                    taskInfoArrayList
                        .toList()
                        .apply { taskInfoArrayList.clear() }
                        .forEach(::clear)
                }
            }

            private fun add(task: Task, key: String?) {
                val taskInfo = TaskInfo(task, id = ++counter, key = key)
                taskInfoArrayList += taskInfo
                logTaskAdded(taskInfo.id, taskInfo.key)
                task.start { synchronized(any) { remove(task) } }
            }

            private fun checkContains(task: Task) {
                val taskInfo = taskInfoArrayList.find { it.task === task }
                taskInfo ?: return
                throw IllegalArgumentException("Task has already been added")
            }

            private fun clear(taskInfo: TaskInfo) {
                logTaskRemoved(taskInfo.id, taskInfo.key)
                taskInfo.task.cancel()
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
                val taskInfo = taskInfoArrayList.find { it.task === task } ?: return
                taskInfoArrayList.remove(taskInfo)
                clear(taskInfo)
            }

        }
    }

    private class TaskInfo(val task: Task, val id: Int, val key: String?)

}