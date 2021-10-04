package ru.g000sha256.reduktor

internal class StoreImpl<A, S>(
    private val initializers: Iterable<Initializer<A, S>>,
    private val sideEffects: Iterable<SideEffect<A, S>>,
    private val logger: Logger,
    private val reducer: Reducer<A, S>,
    private val onNewState: (S) -> Unit,
    private var state: S
) : Store<A> {

    private val lock = Any()

    private val thread: Thread
        get() = Thread.currentThread()

    init {
        logger.invoke("STATE : $state")
        logger.invoke("THREAD: ${thread.name}")
        synchronized(lock) {
            initializers.forEach {
                val state = state
                it.apply { invoke(state) }
            }
        }
    }

    override fun post(action: A) {
        synchronized(lock) {
            val oldState = state
            logger.invoke("--------")
            logger.invoke("ACTION: $action")
            val newState = reducer.run { oldState.invoke(action) }
            if (newState == oldState) {
                logger.invoke("STATE : NOT CHANGED")
                logger.invoke("THREAD: ${thread.name}")
            } else {
                state = newState
                logger.invoke("STATE : $newState")
                logger.invoke("THREAD: ${thread.name}")
                onNewState(newState)
            }
            sideEffects.forEach { it.apply { invoke(action, newState) } }
        }
    }

}