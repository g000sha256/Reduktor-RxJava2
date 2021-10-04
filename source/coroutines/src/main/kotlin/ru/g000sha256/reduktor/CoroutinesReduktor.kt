package ru.g000sha256.reduktor

import kotlinx.coroutines.flow.Flow

interface CoroutinesReduktor<A, S> : Reduktor<A, S> {

    val states: Flow<S>

}