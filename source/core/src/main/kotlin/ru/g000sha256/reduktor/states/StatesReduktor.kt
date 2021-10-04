package ru.g000sha256.reduktor.states

import ru.g000sha256.reduktor.Dispatcher
import ru.g000sha256.reduktor.Logger
import ru.g000sha256.reduktor.Reduktor

internal class StatesReduktor<A, S>(
    private val middlewares: Iterable<Middleware<A, S>>,
    private val logger: Logger,
    private val onNewState: (S) -> Unit,
    initialState: S
) : Reduktor<A, S> {

    private val lock = Any()
    private val dispatcher = Dispatcher(::updateState)

    private var state = initialState

    override fun dispatch(value: A) {
        synchronized(lock) {
            val oldState = state
            logger.invoke("---------")
            logger.invoke("ACTION > $value")
            logger.invoke("STATE  > $oldState")
            val threadName = getThreadName()
            logger.invoke("THREAD   $threadName")
            middlewares.forEach { it.apply { dispatcher.invoke(value, oldState) } }
        }
    }

    private fun getThreadName(): String {
        val thread = Thread.currentThread()
        return thread.name
    }

    private fun updateState(newState: S) {
        synchronized(lock) {
            logger.invoke("---------")
            val threadName = getThreadName()
            if (newState == state) {
                logger.invoke("STATE    NOT CHANGED")
                logger.invoke("THREAD   $threadName")
            } else {
                state = newState
                logger.invoke("STATE  < $newState")
                logger.invoke("THREAD   $threadName")
                onNewState(newState)
            }
        }
    }

}