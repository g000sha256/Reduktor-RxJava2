package ru.g000sha256.reduktor.states

import ru.g000sha256.reduktor.Logger
import ru.g000sha256.reduktor.Reduktor

internal class StatesReduktor<A, S>(
    private val middlewares: Iterable<Middleware<A, S>>,
    private val logger: Logger,
    private val onNewState: (S) -> Unit,
    initialState: S
) : Reduktor<A, S> {

    private val lock = Any()
    private val states = States(::updateState)

    private val thread: String
        get() {
            val thread = Thread.currentThread()
            return thread.name
        }

    private var state = initialState

    override fun dispatch(action: A) {
        synchronized(lock) {
            val oldState = state
            logger.invoke("---------")
            logger.invoke("ACTION > $action")
            logger.invoke("STATE  > $oldState")
            logger.invoke("THREAD   $thread")
            middlewares.forEach { it.apply { states.invoke(action, oldState) } }
        }
    }

    private fun updateState(newState: S) {
        synchronized(lock) {
            logger.invoke("---------")
            if (newState == state) {
                logger.invoke("STATE    NOT CHANGED")
                logger.invoke("THREAD   $thread")
            } else {
                state = newState
                logger.invoke("STATE  < $newState")
                logger.invoke("THREAD   $thread")
                onNewState(newState)
            }
        }
    }

}