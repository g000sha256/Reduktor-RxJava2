package test

infix fun <A> Dispatcher<A>.send(action: A) {
    send(action)
}

infix fun <A> Dispatcher<A>.send(actions: Iterable<A>) {
    send(actions)
}

infix fun TaskCleaner.cancel(key: String) {
    cancel(key)
}

operator fun TaskCreator.plusAssign(creator: TaskCreator.Creator) {
    create(creator)
}

operator fun TaskCreator.set(key: String, creator: TaskCreator.Creator) {
    create(key, creator)
}