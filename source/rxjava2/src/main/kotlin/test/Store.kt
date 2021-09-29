package test

class Store<A, S>(
    initialState: S,
    private val reducer: Reducer<A, S>,
    private val initializers: List<Initializer<A, S>> = emptyList(),
    private val sideEffects: Iterable<SideEffect<A, S>> = emptyList(),
    private val logger: Logger = Logger {}
) {

    var onNewStateCallback: (state: S) -> Unit = {}

    private val lock = Any()
    private val dispatcher = Dispatcher<A> { handleAction(it) }

    private val thread: String
        get() {
            val thread = Thread.currentThread()
            return thread.name
        }

    private var isInitialized = false
    private var state = initialState

    fun start() {
        synchronized(lock) {
            if (isInitialized) return@synchronized
            isInitialized = true
            logger.invoke("|----------")
            logger.invoke("| INIT   : START")
            logger.invoke("| THREAD : $thread")
            logger.invoke("|----------")
            initializers.forEach {
                val state = state
                it.apply { dispatcher.invoke(state) }
                if (!isInitialized) return@synchronized
            }
            onNewStateCallback.invoke(state)
        }
    }

    fun stop() {
        synchronized(lock) {
            if (!isInitialized) return@synchronized
            sideEffects.forEach { it.onCleared() }
            isInitialized = false
            logger.invoke("|----------")
            logger.invoke("| INIT   : STOP")
            logger.invoke("| THREAD : $thread")
            logger.invoke("|----------")
        }
    }

    private fun handleAction(action: A) {
        synchronized(lock) {
            if (!isInitialized) return@synchronized
            val oldState = state
            logger.invoke("|----------")
            logger.invoke("| ACTION > $action")
            logger.invoke("| STATE  > $oldState")
            val newState = reducer.invoke(action, oldState)
            if (newState == oldState) {
                logger.invoke("| STATE  : NOT CHANGED")
                logger.invoke("| THREAD : $thread")
                logger.invoke("|----------")
            } else {
                state = newState
                logger.invoke("| STATE  < $newState")
                logger.invoke("| THREAD : $thread")
                logger.invoke("|----------")
                onNewStateCallback.invoke(newState)
            }
            sideEffects.forEach {
                it.apply { dispatcher.onNewAction(action, newState) }
                if (!isInitialized) return@synchronized
            }
        }
    }

}