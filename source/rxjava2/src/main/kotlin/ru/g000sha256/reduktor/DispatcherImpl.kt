package ru.g000sha256.reduktor

import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Maybe
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Action
import io.reactivex.processors.FlowableProcessor

internal class DispatcherImpl<A>(
    private val disposables: Disposables,
    private val flowableProcessor: FlowableProcessor<A>
) : Dispatcher<A> {

    private val lock = Any()

    override fun cancel(key: String) {
        synchronized(lock) { disposables.remove(key) }
    }

    override fun cancel(vararg keys: String) {
        synchronized(lock) { keys.forEach { disposables.remove(it) } }
    }

    override fun dispatch(action: A) {
        flowableProcessor.onNext(action)
    }

    override fun dispatch(vararg actions: A) {
        actions.forEach { flowableProcessor.onNext(it) }
    }

    override fun dispatch(actions: Iterable<A>) {
        actions.forEach { flowableProcessor.onNext(it) }
    }

    override fun Completable.launch(key: String?) {
        add(key) { action ->
            return@add subscribe(
                { action.run() },
                {
                    flowableProcessor.onError(it)
                    action.run()
                }
            )
        }
    }

    override fun Flowable<A>.launch(key: String?) {
        add(key) { action ->
            return@add subscribe(
                { flowableProcessor.onNext(it) },
                {
                    flowableProcessor.onError(it)
                    action.run()
                },
                { action.run() }
            )
        }
    }

    override fun Maybe<A>.launch(key: String?) {
        add(key) { action ->
            return@add subscribe(
                {
                    flowableProcessor.onNext(it)
                    action.run()
                },
                {
                    flowableProcessor.onError(it)
                    action.run()
                },
                { action.run() }
            )
        }
    }

    override fun Observable<A>.launch(key: String?) {
        add(key) { action ->
            return@add subscribe(
                { flowableProcessor.onNext(it) },
                {
                    flowableProcessor.onError(it)
                    action.run()
                },
                { action.run() }
            )
        }
    }

    override fun Single<A>.launch(key: String?) {
        add(key) { action ->
            return@add subscribe(
                {
                    flowableProcessor.onNext(it)
                    action.run()
                },
                {
                    flowableProcessor.onError(it)
                    action.run()
                }
            )
        }
    }

    private fun add(key: String?, callback: (Action) -> Disposable) {
        synchronized(lock) {
            if (key == null) {
                var disposable: Disposable? = null
                val action = Action { disposable?.apply { disposables -= this } }
                disposable = callback(action)
                if (!disposable.isDisposed) disposables += disposable
            } else {
                disposables.remove(key)
                val action = Action { disposables.remove(key) }
                val disposable = callback(action)
                if (!disposable.isDisposed) disposables[key] = disposable
            }
        }
    }

}