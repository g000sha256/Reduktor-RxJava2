package ru.g000sha256.reduktor.common

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import ru.g000sha256.reduktor.Initializer
import ru.g000sha256.reduktor.task

class ActionsInitializer<A, S>(
    private val coroutineScope: CoroutineScope,
    private val actions: Flow<A>
) : Initializer<A, S> {

    override fun Initializer.Context<A>.invoke(initialState: S) {
        val flow = this@ActionsInitializer.actions
        tasks += coroutineScope.task(Dispatchers.Unconfined) { flow.collect(actions::send) }
    }

}