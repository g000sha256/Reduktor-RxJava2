package ru.g000sha256.reduktor

class Store<A, S>(
    private var state: S,
    private val reducer: Reducer<A, S>,
    private val initializers: Iterable<Initializer<A, S>> = emptyList(),
    private val sideEffects: Iterable<SideEffect<A, S>> = emptyList(),
    private val logger: Logger = Logger {},
    private val onNewState: (state: S) -> Unit
) {

    private val actionsOwner: ActionsOwner<A>
    private val lock = Any()

    private val thread: Thread
        get() = Thread.currentThread()

    init {
        val actions = Actions(::post)
        actionsOwner = ActionsOwnerImpl(actions)
        logger.invoke("STATE : $state")
        logger.invoke("THREAD: ${thread.name}")
        synchronized(lock) { initializers.forEach { it.apply { actionsOwner.invoke(state) } } }
    }

    private fun post(action: A) {
        synchronized(lock) {
            logger.invoke("--------")
            logger.invoke("ACTION: $action")
            val newState = reducer.run { state.invoke(action) }
            if (newState == state) {
                logger.invoke("STATE : NOT CHANGED")
                logger.invoke("THREAD: ${thread.name}")
            } else {
                state = newState
                logger.invoke("STATE : $newState")
                logger.invoke("THREAD: ${thread.name}")
                onNewState(newState)
            }
            sideEffects.forEach { it.apply { actionsOwner.invoke(action, newState) } }
        }
    }

    private class ActionsOwnerImpl<A>(override val actions: Actions<A>) : ActionsOwner<A>

}