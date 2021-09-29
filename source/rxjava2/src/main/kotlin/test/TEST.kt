package test

import io.reactivex.Flowable
import io.reactivex.disposables.Disposable
import ru.g000sha256.reduktor.SideEffect
import ru.g000sha256.reduktor.Task
import ru.g000sha256.reduktor.cancel
import ru.g000sha256.reduktor.send

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

fun <A> Flowable<A>.toTask(
    onNext: (action: A) -> Unit = {},
    onError: (throwable: Throwable) -> Unit = {},
    onComplete: () -> Unit = {}
): Task {
    val flowable = this
    return object : Task {

        override val isCompleted: Boolean
            get() = _isCompleted

        private val lock = Any()

        private var _isCompleted = false
        private var disposable: Disposable? = null

        override fun cancel() {
            synchronized(lock) {
                disposable?.dispose()
                disposable = null
            }
        }

        override fun start() {
            synchronized(lock) {
                if (disposable == null) {
                    disposable = flowable
                        .doOnCancel { }
                        .doOnComplete { }
                        .doOnError { }
                        .subscribe(onNext, onError, onComplete)
                }
            }
        }

    }
}