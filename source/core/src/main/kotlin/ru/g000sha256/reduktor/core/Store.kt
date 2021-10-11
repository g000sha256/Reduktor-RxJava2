package ru.g000sha256.reduktor.core

class Store<A, S>(
    initialState: S,
    private val reducer: Reducer<A, S>,
    initializers: Iterable<Initializer<A, S>> = emptyList(),
    private val sideEffects: Iterable<SideEffect<A, S>> = emptyList(),
    private val logger: Logger = Logger {},
    private val onNewState: (state: S) -> Unit
) {

    private val actionsOwner = createActionsOwner()
    private val lock = Any()

    private var isReleased = false
    private var state = initialState

    init {
        logger.invoke("STATE  : $initialState")
        logger.logThread()
        onNewState(initialState)
        initializers.forEach { it.apply { actionsOwner.invoke(initialState) } }
    }

    fun release() {
        synchronized(lock) { isReleased = true }
    }

    private fun createActions(): Actions<A> {
        return object : Actions<A> {

            override fun post(action: A) {
                synchronized(lock) {
                    if (isReleased) return@synchronized
                    handleAction(action)
                }

            }

            override fun post(vararg actions: A) {
                synchronized(lock) {
                    if (isReleased) return@synchronized
                    actions.forEach(::handleAction)
                }
            }

            override fun post(actions: Iterable<A>) {
                synchronized(lock) {
                    if (isReleased) return@synchronized
                    actions.forEach(::handleAction)
                }
            }

        }
    }

    private fun createActionsOwner(): ActionsOwner<A> {
        return object : ActionsOwner<A> {

            override val actions = createActions()

        }
    }

    private fun handleAction(action: A) {
        val oldState = state
        logger.invoke("---------")
        logger.invoke("ACTION > $action")
        logger.invoke("STATE  > $oldState")
        val newState = reducer.run { oldState.invoke(action) }
        if (newState == oldState) {
            logger.invoke("STATE  : NOT CHANGED")
            logger.logThread()
        } else {
            state = newState
            logger.invoke("STATE  < $newState")
            logger.logThread()
            onNewState(newState)
        }
        sideEffects.forEach { it.apply { actionsOwner.invoke(action, newState) } }
    }

    private fun Logger.logThread() {
        val thread = Thread.currentThread()
        invoke("THREAD : ${thread.name}")
    }

}