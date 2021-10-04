package ru.g000sha256.reduktor

import kotlinx.coroutines.flow.Flow

interface CoroutinesReduktor<A, S> : Reduktor<A> {

    val states: Flow<S>

}