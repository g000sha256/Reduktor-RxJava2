package test1

class Store<A, S>(
    initialState: S,
    private val reducer: Reducer<A, S>,
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
            logger.log("|----------")
            logger.log("| INIT   : START")
            logger.log("| THREAD : $thread")
            logger.log("|----------")
            sideEffects.forEach {
                val state = state
                it.apply { dispatcher.onInit(state) }
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
            logger.log("|----------")
            logger.log("| INIT   : STOP")
            logger.log("| THREAD : $thread")
            logger.log("|----------")
        }
    }

    private fun handleAction(action: A) {
        synchronized(lock) {
            if (!isInitialized) return@synchronized
            val oldState = state
            logger.log("|----------")
            logger.log("| ACTION > $action")
            logger.log("| STATE  > $oldState")
            val newState = reducer.reduce(action, oldState)
            if (newState == oldState) {
                logger.log("| STATE  : NOT CHANGED")
                logger.log("| THREAD : $thread")
                logger.log("|----------")
            } else {
                state = newState
                logger.log("| STATE  < $newState")
                logger.log("| THREAD : $thread")
                logger.log("|----------")
                onNewStateCallback.invoke(newState)
            }
            sideEffects.forEach {
                it.apply { dispatcher.onNewAction(action, newState) }
                if (!isInitialized) return@synchronized
            }
        }
    }

}