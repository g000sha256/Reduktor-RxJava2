package ru.g000sha256.reduktor.actions

import ru.g000sha256.reduktor.Logger
import ru.g000sha256.reduktor.Reduktor

internal class ActionsReduktor<A, S>(
    private val initializers: Iterable<Initializer<A, S>>,
    private val sideEffects: Iterable<SideEffect<A, S>>,
    private val logger: Logger,
    private val reducer: Reducer<A, S>,
    private val onNewState: (S) -> Unit,
    private var state: S
) : Reduktor<A> {

    private val actions = Actions(::dispatch)
    private val lock = Any()

    private val thread: Thread
        get() = Thread.currentThread()

    init {
        logger.invoke("STATE : $state")
        logger.invoke("THREAD: ${thread.name}")
        synchronized(lock) {
            initializers.forEach {
                val state = state
                it.apply { actions.invoke(state) }
            }
        }
    }

    override fun dispatch(action: A) {
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
            sideEffects.forEach { it.apply { actions.invoke(action, newState) } }
        }
    }

}