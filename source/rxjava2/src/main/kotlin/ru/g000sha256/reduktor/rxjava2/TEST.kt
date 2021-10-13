package ru.g000sha256.reduktor.rxjava2

import io.reactivex.Single
import ru.g000sha256.reduktor.core.Environment

fun Environment<String>.test() {
    tasks += Single
        .just("...")
        .toTask(actions::post)
    tasks["1"] = Single
        .just("...")
        .toTask(actions::post)
}