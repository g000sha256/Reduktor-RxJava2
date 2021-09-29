package ru.g000sha256.reduktor.common

import kotlinx.coroutines.CoroutineScope

inline fun <A, S, reified T> createNewsSideEffect(coroutineScope: CoroutineScope): NewsSideEffect<A, S, T> {
    val clazz = T::class.java
    return createNewsSideEffect(coroutineScope, clazz)
}

fun <A, S, T> createNewsSideEffect(coroutineScope: CoroutineScope, clazz: Class<T>): NewsSideEffect<A, S, T> {
    return object : NewsSideEffect<A, S, T>(coroutineScope) {

        override fun map(action: A, state: S): T? {
            val isInstance = clazz.isInstance(action)
            if (isInstance) return action as T
            return null
        }

    }
}

fun <A, S, T> createNewsSideEffect(
    coroutineScope: CoroutineScope,
    callback: (action: A, state: S) -> T?
): NewsSideEffect<A, S, T> {
    return object : NewsSideEffect<A, S, T>(coroutineScope) {

        override fun map(action: A, state: S): T? {
            return callback(action, state)
        }

    }
}