package test

import g000sha256.reduktor.core.Environment
import g000sha256.reduktor.core.plusAssign
import g000sha256.reduktor.core.post
import g000sha256.reduktor.core.set
import g000sha256.reduktor.coroutines.createTask
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

fun Environment<String>.test(coroutineScope: CoroutineScope) {
    tasks += coroutineScope.createTask(Dispatchers.IO) { actions post "..." }
    tasks["1"] = coroutineScope.createTask(Dispatchers.IO) { actions post "..." }
}