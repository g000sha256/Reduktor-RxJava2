package ru.g000sha256.reduktor

infix fun <A> Sender<A>.send(action: A) {
    send(action)
}

infix fun <A> Sender<A>.send(actions: Array<A>) {
    send(*actions)
}

infix fun <A> Sender<A>.send(actions: Iterable<A>) {
    send(actions)
}

infix fun SideEffect.Context.Tasks.cancel(key: String) {
    cancel(key)
}