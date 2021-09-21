package ru.g000sha256.reduktor.common

import kotlinx.coroutines.flow.Flow
import ru.g000sha256.reduktor.Dispatcher
import ru.g000sha256.reduktor.Initializer

class ActionsInitializer<A, S>(private val actions: Flow<A>) : Initializer<A, S> {

    override fun Dispatcher<A>.invoke(initialState: S) {
        actions.launch()
    }

}