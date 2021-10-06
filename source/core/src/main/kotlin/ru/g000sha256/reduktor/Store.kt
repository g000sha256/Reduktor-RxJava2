package ru.g000sha256.reduktor

class Store<A, S>(
    initialState: S,
    private val reducer: Reducer<A, S>,
    private val initializers: Iterable<Initializer<A, S>> = emptyList(),
    private val sideEffects: Iterable<SideEffect<A, S>> = emptyList(),
    private val logger: Logger = Logger {},
    private val onNewState: (state: S) -> Unit
) {

    private val actionsOwner: ActionsOwner<A>
    private val lock = Any()

    private var state = initialState

    init {
        val actions = object : Actions<A> {

            override fun post(action: A) {
                handleAction(action)
            }

            override fun post(vararg actions: A) {
                actions.forEach(::handleAction)
            }

            override fun post(actions: Iterable<A>) {
                actions.forEach(::handleAction)
            }

        }
        actionsOwner = ActionsOwnerImpl(actions)
        logger.apply {
            invoke("STATE  : $state")
            logThread()
        }
        synchronized(lock) {
            val state = state
            initializers.forEach { it.apply { actionsOwner.invoke(state) } }
        }
    }

    private fun handleAction(action: A) {
        synchronized(lock) {
            val oldState = state
            logger.apply {
                logSeparator()
                invoke("ACTION > $action")
                invoke("STATE  > $oldState")
            }
            val newState = reducer.run { oldState.invoke(action) }
            if (newState == oldState) {
                logger.apply {
                    invoke("STATE  : NOT CHANGED")
                    logThread()
                }
            } else {
                state = newState
                logger.apply {
                    invoke("STATE  < $newState")
                    logThread()
                }
                onNewState(newState)
            }
            sideEffects.forEach { it.apply { actionsOwner.invoke(action, newState) } }
        }
    }

    private class ActionsOwnerImpl<A>(override val actions: Actions<A>) : ActionsOwner<A>

}