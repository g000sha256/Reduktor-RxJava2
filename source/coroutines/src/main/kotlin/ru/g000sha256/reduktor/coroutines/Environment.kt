package ru.g000sha256.reduktor.coroutines

import kotlinx.coroutines.CoroutineScope

interface Environment<A> {

    val actions: Actions<A>
    val coroutineScope: CoroutineScope
    val tasks: Tasks

}