package g000sha256.reduktor.core

fun <A, S> Store(
    initialState: S,
    reducer: Reducer<A, S>,
    initializers: List<Initializer<A, S>> = ArrayList(),
    sideEffects: List<SideEffect<A, S>> = ArrayList(),
    logger: Logger = Logger {},
    statesCallback: (state: S) -> Unit
): Store<A, S> {
    return Store(logger, reducer, statesCallback, initialState, initializers, sideEffects)
}

infix fun <A> Actions<A>.post(action: A) {
    post(action)
}

infix fun <A> Actions<A>.post(actions: Array<A>) {
    post(*actions)
}

infix fun <A> Actions<A>.post(actions: Iterable<A>) {
    post(actions)
}

infix fun Tasks.clear(key: String) {
    clear(key)
}

operator fun Tasks.plusAssign(task: Task) {
    add(task)
}

operator fun Tasks.set(key: String, task: Task) {
    add(key, task)
}