package ru.g000sha256.reduktor.common

fun <A, S, T> NewsSideEffect(clazz: Class<T>): NewsSideEffect<A, S, T> {
    return object : NewsSideEffect<A, S, T>() {

        override fun map(action: A, state: S): T? {
            val isInstance = clazz.isInstance(action)
            if (isInstance) return action as T
            return null
        }

    }
}

fun <A, S, T> NewsSideEffect(callback: (action: A, state: S) -> T?): NewsSideEffect<A, S, T> {
    return object : NewsSideEffect<A, S, T>() {

        override fun map(action: A, state: S): T? {
            return callback(action, state)
        }

    }
}