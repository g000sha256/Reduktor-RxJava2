package test

import io.reactivex.Flowable

object Action

private object State

private class SideEffectImpl : SideEffect<Action, State> {

    override fun SideEffect.Context<Action, State>.invoke(newAction: Action) {
        // Получаем текущее актуальное состояние
        val state = stateAccessor.currentState

        // Отправка новых Action
        dispatcher.send(Action)
        dispatcher.send(Action, Action, Action)
        dispatcher.send(listOf(Action, Action, Action))
        dispatcher send Action
        dispatcher send listOf(Action, Action, Action)

        // Отмена задачи
        taskCleaner.cancel(key = "task_1")
        taskCleaner cancel "task_1"

        // Создание задачи
        taskCreator.create {
            return@create Flowable
                .just(Action)
                .subscribe(dispatcher::send)
        }
        taskCreator.create(key = "task_1") {
            return@create Flowable
                .just(Action)
                .subscribe(dispatcher::send)
        }
        taskCreator += TaskCreator.Creator {
            return@Creator Flowable
                .just(Action)
                .subscribe(dispatcher::send)
        }
        taskCreator["task_1"] = TaskCreator.Creator {
            return@Creator Flowable
                .just(Action)
                .subscribe(dispatcher::send)
        }
    }

}