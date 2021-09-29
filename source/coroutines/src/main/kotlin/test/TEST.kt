package test

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay

object Action

private object State

private class SideEffectImpl(private val coroutineScope: CoroutineScope) : SideEffect<Action, State> {

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
        tasks += coroutineScope.task {
            delay(500L)
            actions send Action
        }
        tasks["task_1"] = coroutineScope.task {
            delay(500L)
            actions send Action
        }
    }

}

fun CoroutineScope.task(callback: suspend CoroutineScope.() -> Unit): Task {

}