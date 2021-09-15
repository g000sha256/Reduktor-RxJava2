package ru.g000sha256.reduktor

import kotlin.coroutines.CoroutineContext
import kotlinx.coroutines.CoroutineDispatcher

internal object ImmediateCoroutineDispatcher : CoroutineDispatcher() {

    override fun toString(): String {
        return "ImmediateCoroutineDispatcher"
    }

    override fun isDispatchNeeded(context: CoroutineContext): Boolean {
        return false
    }

    override fun dispatch(context: CoroutineContext, block: Runnable) {}

}