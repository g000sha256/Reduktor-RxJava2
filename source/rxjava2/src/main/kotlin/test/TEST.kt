package test

import io.reactivex.Flowable

object Action

private object State

private class SideEffectImpl : SideEffect<Action, State> {

    override fun SideEffect.Context<Action, State>.invoke(newAction: Action) {
        // Получаем текущее актуальное состояние
        val state = currentState

        // Отправка новых Action
        actions.send(Action)
        actions.send(Action, Action, Action)
        actions.send(listOf(Action, Action, Action))
        actions send Action
        actions send listOf(Action, Action, Action)
        actions + Action
        actions + listOf(Action, Action, Action)
        actions += Action
        actions += listOf(Action, Action, Action)

        // Отмена задачи
        jobs.cancel(key = "task_1")
        jobs cancel "task_1"
        jobs - "task_1"
        jobs -= "task_1"

        // Создание задачи
        jobs.create {
            return@create Flowable
                .just(Action)
                .subscribe(actions::send)
        }
        jobs.create(key = "task_1") {
            return@create Flowable
                .just(Action)
                .subscribe(actions::send)
        }
        jobs += TaskCreator.Creator {
            return@Creator Flowable
                .just(Action)
                .subscribe(actions::send)
        }
        jobs["task_1"] = TaskCreator.Creator {
            return@Creator Flowable
                .just(Action)
                .subscribe(actions::send)
        }
    }

}