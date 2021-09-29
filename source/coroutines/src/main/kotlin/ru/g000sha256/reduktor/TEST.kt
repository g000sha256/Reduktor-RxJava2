package ru.g000sha256.reduktor

import kotlinx.coroutines.delay

object Action

private object State

private class SideEffectImpl : SideEffect<Action, State> {

    override fun SideEffect.Context<Action>.invoke(action: Action, state: State) {
        // Отправка новых Action
        sender.send(Action)
        sender.send(Action, Action, Action)
        sender.send(listOf(Action, Action, Action))
        sender send Action
        sender send arrayOf(Action, Action, Action)
        sender send listOf(Action, Action, Action)

        // Отмена задачи
        tasks.cancel(key = "task_1")
        tasks cancel "task_1"

        // Создание задачи
        tasks += unconfinedTask {
            delay(500L)
            sender send Action
        }
        tasks["task_1"] = unconfinedTask {
            delay(500L)
            sender send Action
        }
    }

}