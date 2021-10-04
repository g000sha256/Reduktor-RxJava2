package ru.g000sha256.reduktor.actions

import ru.g000sha256.reduktor.Logger
import ru.g000sha256.reduktor.Reduktor

internal class ActionsReduktor<A, S>(
    private val initializers: Iterable<Initializer<A, S>>,
    private val sideEffects: Iterable<SideEffect<A, S>>,
    private val logger: Logger,
    private val reducer: Reducer<A, S>,
    private val onNewState: (S) -> Unit,
    initialState: S
) : Reduktor<A, S> {

    private val lock = Any()

    private var state = initialState

    init {
        synchronized(lock) {
            initializers.forEach {
                val state = state
                it.apply { invoke(state) }
            }
        }
    }

    override fun dispatch(value: A) {
        synchronized(lock) {
            val oldState = state
            logger.invoke("---------")
            logger.invoke("ACTION > $value")
            logger.invoke("STATE  > $oldState")
            val newState = reducer.run { oldState.invoke(value) }
            val threadName = getThreadName()
            if (newState == oldState) {
                logger.invoke("STATE    NOT CHANGED")
                logger.invoke("THREAD   $threadName")
            } else {
                state = newState
                logger.invoke("STATE  < $newState")
                logger.invoke("THREAD   $threadName")
                onNewState(newState)
            }
            sideEffects.forEach { it.apply { invoke(value, newState) } }
        }
    }

    private fun getThreadName(): String {
        val thread = Thread.currentThread()
        return thread.name
    }

}