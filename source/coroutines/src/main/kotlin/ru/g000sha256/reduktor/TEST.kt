package ru.g000sha256.reduktor

import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay

object Action

private object State

private class SideEffectImpl(private val coroutineScope: CoroutineScope) : SideEffect<Action, State> {

    override fun SideEffect.Context<Action>.invoke(action: Action, state: State) {
        // Отправка новых Action
        actions.post(Action)
        actions.post(Action, Action, Action)
        actions.post(listOf(Action, Action, Action))
        actions post Action
        actions post arrayOf(Action, Action, Action)
        actions post listOf(Action, Action, Action)

        // Отмена задачи
        tasks.cancel(key = "task_1")
        tasks cancel "task_1"

        // Создание задачи
        tasks += coroutineScope.createTask {
            delay(500L)
            actions post Action
        }
        tasks["task_1"] = coroutineScope.createTask {
            delay(500L)
            actions post Action
        }
    }

}

fun CoroutineScope.createTask(
    context: CoroutineContext = EmptyCoroutineContext,
    block: suspend CoroutineScope.() -> Unit
): Task