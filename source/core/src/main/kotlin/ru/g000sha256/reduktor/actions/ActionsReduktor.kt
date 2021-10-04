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

    private val actions: Actions<A>
    private val lock = Any()

    private val thread: String
        get() {
            val thread = Thread.currentThread()
            return thread.name
        }

    private var state = initialState

    init {
        actions = object : Actions<A> {

            override fun post(action: A) {
                dispatch(action)
            }

            override fun post(vararg actions: A) {
                actions.forEach(::dispatch)
            }

            override fun post(actions: Iterable<A>) {
                actions.forEach(::dispatch)
            }

        }
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
            logger.invoke("---------")
            logger.invoke("ACTION > $action")
            logger.invoke("STATE  > $oldState")
            val newState = reducer.run { oldState.invoke(action) }
            if (newState == oldState) {
                logger.invoke("STATE    NOT CHANGED")
                logger.invoke("THREAD   $thread")
            } else {
                state = newState
                logger.invoke("STATE  < $newState")
                logger.invoke("THREAD   $thread")
                onNewState(newState)
            }
            sideEffects.forEach { it.apply { actions.invoke(action, newState) } }
        }
    }

}