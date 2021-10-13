package ru.g000sha256.reduktor.core

interface Environment<A> {

    val actions: Actions<A>
    val tasks: Tasks

}