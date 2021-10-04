package ru.g000sha256.reduktor.states

import ru.g000sha256.reduktor.Logger
import ru.g000sha256.reduktor.Reduktor

internal class StatesReduktor<A, S>(
    private val middlewares: Iterable<Middleware<A, S>>,
    private val logger: Logger,
    private val onNewState: (S) -> Unit,
    private var state: S
) : Reduktor<A> {

    private val lock = Any()
    private val states = States(::updateState)

    private val thread: Thread
        get() = Thread.currentThread()

    override fun dispatch(action: A) {
        synchronized(lock) {
            logger.invoke("---------")
            logger.invoke("ACTION > $action")
            logger.invoke("STATE  > $state")
            logger.invoke("THREAD   ${thread.name}")
            middlewares.forEach { it.apply { states.invoke(action, state) } }
        }
    }

    private fun updateState(newState: S) {
        synchronized(lock) {
            logger.invoke("---------")
            if (newState == state) {
                logger.invoke("STATE    NOT CHANGED")
                logger.invoke("THREAD   ${thread.name}")
            } else {
                state = newState
                logger.invoke("STATE  < $newState")
                logger.invoke("THREAD   ${thread.name}")
                onNewState(newState)
            }
        }
    }

}