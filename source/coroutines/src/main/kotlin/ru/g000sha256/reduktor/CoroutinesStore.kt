package ru.g000sha256.reduktor

import kotlinx.coroutines.flow.Flow

interface CoroutinesStore<A, S> : Store<A> {

    val states: Flow<S>

}