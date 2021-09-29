package ru.g000sha256.reduktor

import io.reactivex.Flowable
import io.reactivex.processors.BehaviorProcessor

class Store<A, S>(
    initialState: S,
    private val reducer: Reducer<A, S>,
    private val initializers: Iterable<Initializer<A, S>> = emptyList(),
    private val sideEffects: Iterable<SideEffect<A, S>> = emptyList(),
    private val logger: Logger = Logger {}
) {

    val states: Flowable<S>

    private val lock = Any()
    private val behaviorProcessor = BehaviorProcessor.createDefault(initialState)

    private val thread: String
        get() {
            val thread = Thread.currentThread()
            return thread.name
        }

    private var init = Init.NONE
    private var state = initialState

    init {
        states = behaviorProcessor.onBackpressureLatest()
    }

    fun start() {
        synchronized(lock) {
            if (init == Init.STOP_BEGIN) throw IllegalStateException() // ToDo
            if (init == Init.START_BEGIN) return@synchronized
            if (init == Init.START_END) return@synchronized
            init = Init.START_BEGIN
            logger.invoke("INIT     START")
            logger.invoke("THREAD   $thread")
            initializers.forEach {
                val state = state
                it.apply { dispatcher.invoke(state) }
            }
            init = Init.START_END
        }
    }

    fun stop() {
        synchronized(lock) {
            if (init == Init.START_BEGIN) throw IllegalStateException() // ToDo
            if (init == Init.NONE) return@synchronized
            if (init == Init.STOP_BEGIN) return@synchronized
            if (init == Init.STOP_END) return@synchronized
            init = Init.STOP_BEGIN
            sideEffects.forEach { it.onCleared() }
            logger.invoke("---------")
            logger.invoke("INIT     STOP")
            logger.invoke("THREAD   $thread")
            init = Init.STOP_END
        }
    }

    private fun handleAction(action: A) {
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
                behaviorProcessor.onNext(newState)
            }
            sideEffects.forEach { it.apply { dispatcher.onNewAction(action, newState) } }
        }
    }

    private enum class Init {

        NONE,
        START_BEGIN,
        START_END,
        STOP_BEGIN,
        STOP_END

    }

}