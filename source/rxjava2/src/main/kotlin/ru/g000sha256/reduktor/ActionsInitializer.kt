package ru.g000sha256.reduktor

import io.reactivex.Flowable

class ActionsInitializer<A, S>(private val actions: Flowable<A>) : Initializer<A, S> {

    override fun Initializer.Context<A>.invoke(initialState: S) {
        tasks += this@ActionsInitializer
            .actions
            .toTask(actions::send)
    }

}