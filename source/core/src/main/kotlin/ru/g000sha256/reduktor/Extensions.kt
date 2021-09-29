package ru.g000sha256.reduktor

infix fun <A> Actions<A>.send(action: A) {
    send(action)
}

infix fun <A> Actions<A>.send(actions: Array<A>) {
    send(*actions)
}

infix fun <A> Actions<A>.send(actions: Iterable<A>) {
    send(actions)
}

infix fun SideEffect.Context.Tasks.cancel(key: String) {
    cancel(key)
}