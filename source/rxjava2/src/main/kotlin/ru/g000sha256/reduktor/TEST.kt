package ru.g000sha256.reduktor

import io.reactivex.Flowable

object Action

private object State

private class SideEffectImpl : SideEffect<Action, State> {

    override fun SideEffect.Context<Action>.invoke(action: Action, state: State) {
        // Отправка новых Action
        actions.send(Action)
        actions.send(Action, Action, Action)
        actions.send(listOf(Action, Action, Action))
        actions send Action
        actions send arrayOf(Action, Action, Action)
        actions send listOf(Action, Action, Action)

        // Отмена задачи
        tasks.cancel(key = "task_1")
        tasks cancel "task_1"

        // Создание задачи
        tasks += Flowable
            .just(Action)
            .toTask(actions::send)
        tasks["task_1"] = Flowable
            .just(Action)
            .toTask(actions::send)
    }

}