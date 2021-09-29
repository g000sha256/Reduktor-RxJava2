package test

import io.reactivex.Flowable

object Action

private object State

private class SideEffectImpl : SideEffect<Action, State> {

    override fun SideEffect.Context<Action, State>.invoke(newAction: Action) {
        // Получаем текущее актуальное состояние
        val state = currentState

        // Отправка новых Action
        dispatch(Action)
        dispatch(Action, Action, Action)
        dispatch(listOf(Action, Action, Action))

        // Отмена задачи
        cancel(key = "task_1")

        // Создание задачи
        create {
            return@create Flowable
                .just(Action)
                .subscribe(::dispatch)
        }
        create(key = "task_1") {
            return@create Flowable
                .just(Action)
                .subscribe(::dispatch)
        }
    }

}