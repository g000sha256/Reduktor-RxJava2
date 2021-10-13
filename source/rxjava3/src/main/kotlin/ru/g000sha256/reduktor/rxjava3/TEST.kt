package ru.g000sha256.reduktor.rxjava3

import io.reactivex.rxjava3.core.Single
import ru.g000sha256.reduktor.core.Environment
import ru.g000sha256.reduktor.core.plusAssign
import ru.g000sha256.reduktor.core.set

fun Environment<String>.test() {
    tasks += Single
        .just("...")
        .toTask(actions::post)
    tasks["1"] = Single
        .just("...")
        .toTask(actions::post)
}