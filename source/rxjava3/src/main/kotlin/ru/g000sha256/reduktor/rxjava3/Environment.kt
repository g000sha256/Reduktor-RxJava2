package ru.g000sha256.reduktor.rxjava3

interface Environment<A> {

    val actions: Actions<A>
    val tasks: Tasks

}