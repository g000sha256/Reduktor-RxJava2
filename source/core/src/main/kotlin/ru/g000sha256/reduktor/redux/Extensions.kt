package ru.g000sha256.reduktor.redux

infix fun <A> Actions<A>.post(action: A) {
    post(action)
}

infix fun <A> Actions<A>.post(actions: Array<A>) {
    post(*actions)
}

infix fun <A> Actions<A>.post(actions: Iterable<A>) {
    post(actions)
}