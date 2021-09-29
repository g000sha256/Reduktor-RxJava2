package test

import kotlinx.coroutines.launch

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
        jobs.cancel(key = "task_1")
        jobs cancel "task_1"

        // Создание задачи
        jobs += scope.launch { actions send Action }
        jobs["task_1"] = scope.launch { actions send Action }
    }

}