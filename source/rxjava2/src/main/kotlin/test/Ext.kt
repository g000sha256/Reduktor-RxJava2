package test

infix fun <A> Actions<A>.send(action: A) {
    send(action)
}

infix fun <A> Actions<A>.send(actions: Iterable<A>) {
    send(actions)
}

operator fun <A> Actions<A>.plus(action: A) {
    send(action)
}

operator fun <A> Actions<A>.plus(actions: Iterable<A>) {
    send(actions)
}

operator fun <A> Actions<A>.plusAssign(action: A) {
    send(action)
}

operator fun <A> Actions<A>.plusAssign(actions: Iterable<A>) {
    send(actions)
}

infix fun TaskCleaner.cancel(key: String) {
    cancel(key)
}

operator fun TaskCleaner.minus(key: String) {
    cancel(key)
}

operator fun TaskCleaner.minusAssign(key: String) {
    cancel(key)
}

operator fun TaskCreator.plusAssign(creator: TaskCreator.Creator) {
    create(creator)
}

operator fun TaskCreator.set(key: String, creator: TaskCreator.Creator) {
    create(key, creator)
}