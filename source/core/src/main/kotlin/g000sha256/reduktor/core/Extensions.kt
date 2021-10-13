package g000sha256.reduktor.core

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