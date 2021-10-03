package ru.g000sha256.reduktor

import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Maybe
import io.reactivex.Observable
import io.reactivex.Single

object Action

private object State

private class SideEffectImpl : SideEffect<Action, State> {

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
        tasks += Flowable
            .just(Action)
            .toTask(actions::post)
        tasks["task_1"] = Flowable
            .just(Action)
            .toTask(actions::post)
    }

}

fun Completable.toTask(
    onComplete: () -> Unit = {},
    onError: (throwable: Throwable) -> Unit = {}
): Task

fun <A> Flowable<A>.toTask(
    onNext: (action: A) -> Unit = {},
    onError: (throwable: Throwable) -> Unit = {},
    onComplete: () -> Unit = {}
): Task

fun <A> Maybe<A>.toTask(
    onSuccess: (action: A) -> Unit = {},
    onError: (throwable: Throwable) -> Unit = {},
    onComplete: () -> Unit = {}
): Task

fun <A> Observable<A>.toTask(
    onNext: (action: A) -> Unit = {},
    onError: (throwable: Throwable) -> Unit = {},
    onComplete: () -> Unit = {}
): Task

fun <A> Single<A>.toTask(
    onSuccess: (action: A) -> Unit = {},
    onError: (throwable: Throwable) -> Unit = {}
): Task