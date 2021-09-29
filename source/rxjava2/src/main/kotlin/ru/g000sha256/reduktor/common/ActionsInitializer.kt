package ru.g000sha256.reduktor.common

import io.reactivex.Flowable
import ru.g000sha256.reduktor.Initializer

class ActionsInitializer<A, S>(private val actions: Flowable<A>) : Initializer<A, S> {

    override fun Initializer.Context<A>.invoke(initialState: S) {
        tasks += actions.toTask(sender::send)
    }

}