package ru.g000sha256.reduktor

infix fun <A> Actions<A>.post(action: A) {
    post(action)
}