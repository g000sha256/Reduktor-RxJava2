package test

infix fun <A> Actions<A>.send(action: A) {
    send(action)
}

infix fun <A> Actions<A>.send(actions: Array<A>) {
    send(*actions)
}

infix fun <A> Actions<A>.send(actions: Iterable<A>) {
    send(actions)
}