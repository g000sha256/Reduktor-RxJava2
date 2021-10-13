package g000sha256.reduktor.rxjava2

import g000sha256.reduktor.core.Environment
import g000sha256.reduktor.core.plusAssign
import g000sha256.reduktor.core.set
import io.reactivex.Single

fun Environment<String>.test() {
    tasks += Single
        .just("...")
        .toTask(actions::post)
    tasks["1"] = Single
        .just("...")
        .toTask(actions::post)
}