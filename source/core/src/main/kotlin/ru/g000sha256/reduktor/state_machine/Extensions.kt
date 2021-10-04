package ru.g000sha256.reduktor.state_machine

infix fun <S> States<S>.post(state: S) {
    post(state)
}