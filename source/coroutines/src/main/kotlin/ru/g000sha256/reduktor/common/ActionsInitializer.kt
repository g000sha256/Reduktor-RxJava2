package ru.g000sha256.reduktor.common

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import ru.g000sha256.reduktor.Initializer

class ActionsInitializer<A, S>(private val actions: Flow<A>) : Initializer<A, S> {

    override fun Initializer.Context<A>.invoke(initialState: S) {
        tasks += unconfinedTask { actions.collect(sender::send) }
    }

}