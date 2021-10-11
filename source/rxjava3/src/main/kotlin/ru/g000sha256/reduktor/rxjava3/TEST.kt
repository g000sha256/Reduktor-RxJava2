package ru.g000sha256.reduktor.rxjava3

import io.reactivex.rxjava3.core.Single

fun Environment<String>.test() {
    tasks += Single
        .just("...")
        .toTask(actions::post)
    tasks["1"] = Single
        .just("...")
        .toTask(actions::post)
}