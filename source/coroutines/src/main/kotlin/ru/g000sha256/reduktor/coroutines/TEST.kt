package ru.g000sha256.reduktor.coroutines

import kotlinx.coroutines.Dispatchers

fun Environment<String>.test() {
    tasks.clear(key = "1")

    tasks += coroutineScope.createTask(Dispatchers.IO) { actions post "..." }
    tasks["1"] = coroutineScope.createTask(Dispatchers.IO) { actions post "..." }
}