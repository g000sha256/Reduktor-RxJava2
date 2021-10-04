package ru.g000sha256.reduktor

abstract class Reduktor<A, S> {

    abstract fun dispatch(action: A)

    abstract fun start()

    abstract fun stop()

    protected abstract fun onNewState(state: S)

}