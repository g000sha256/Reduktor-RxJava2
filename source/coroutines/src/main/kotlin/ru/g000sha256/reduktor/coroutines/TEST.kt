package ru.g000sha256.reduktor.coroutines

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import ru.g000sha256.reduktor.core.Environment
import ru.g000sha256.reduktor.core.plusAssign
import ru.g000sha256.reduktor.core.post
import ru.g000sha256.reduktor.core.set

fun Environment<String>.test(coroutineScope: CoroutineScope) {
    tasks += coroutineScope.createTask(Dispatchers.IO) { actions post "..." }
    tasks["1"] = coroutineScope.createTask(Dispatchers.IO) { actions post "..." }
}