package test

import io.reactivex.Single

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
        disposables.cancel(key = "task_1")
        disposables cancel "task_1"

        // Создание задачи
        disposables += Single
            .just(Action)
            .subscribe(actions::send)
        disposables["task_1"] = Single
            .just(Action)
            .subscribe(actions::send)
    }

}