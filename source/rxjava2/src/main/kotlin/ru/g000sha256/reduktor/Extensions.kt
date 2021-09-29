package ru.g000sha256.reduktor

inline fun <A, S, reified T> createNewsSideEffect(): NewsSideEffect<A, S, T> {
    val clazz = T::class.java
    return createNewsSideEffect(clazz)
}

fun <A, S, T> createNewsSideEffect(clazz: Class<T>): NewsSideEffect<A, S, T> {
    return object : NewsSideEffect<A, S, T>() {

        override fun map(action: A, state: S): T? {
            val isInstance = clazz.isInstance(action)
            if (isInstance) return action as T
            return null
        }

    }
}

fun <A, S, T> createNewsSideEffect(callback: (action: A, state: S) -> T?): NewsSideEffect<A, S, T> {
    return object : NewsSideEffect<A, S, T>() {

        override fun map(action: A, state: S): T? {
            return callback(action, state)
        }

    }
}