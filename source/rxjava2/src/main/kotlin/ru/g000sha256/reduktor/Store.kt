package ru.g000sha256.reduktor

import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Scheduler
import io.reactivex.disposables.Disposable
import io.reactivex.processors.BehaviorProcessor
import io.reactivex.processors.FlowableProcessor
import io.reactivex.processors.PublishProcessor

class Store<A, S>(
    initialState: S,
    private val reducer: Reducer<A, S>,
    private val initializers: List<Initializer<A, S>> = emptyList(),
    private val sideEffects: List<SideEffect<A, S>> = emptyList(),
    private val scheduler: Scheduler? = null,
    private val logger: Logger? = null
) {

    val states: Flowable<S>

    private val stateLock = Any()
    private val subscriptionLock = Any()
    private val dispatcher: Dispatcher<A>
    private val disposables = Disposables()
    private val actionsFlowableProcessor: FlowableProcessor<A> = PublishProcessor.create()
    private val statesFlowableProcessor: FlowableProcessor<S> = BehaviorProcessor.createDefault(initialState)

    private var subscriptions = 0
    private var state = initialState

    init {
        dispatcher = Dispatcher(actionsFlowableProcessor, disposables)
        states = statesFlowableProcessor
            .doOnCancel { synchronized(subscriptionLock) { if (--subscriptions == 0) disposables.clear() } }
            .doOnSubscribe { synchronized(subscriptionLock) { if (++subscriptions == 1) subscribe() } }
            .onBackpressureLatest()
    }

    private fun handleAction(action: A) {
        val logger = logger
        logger?.invoke("ACTION --> $action")
        val oldState = state
        logger?.invoke("STATE  --> $oldState")
        val newState = reducer.invoke(action, oldState)
        if (newState == oldState) {
            logger?.invoke("STATE      NOT CHANGED")
        } else {
            state = newState
            logger?.invoke("STATE  <-- $newState")
            statesFlowableProcessor.onNext(newState)
        }
        sideEffects.forEach { it.apply { dispatcher.invoke(action, newState) } }
    }

    private fun subscribe() {
        disposables += actionsFlowableProcessor
            .let { flowable -> scheduler?.let { flowable.observeOn(it) } ?: flowable }
            .subscribe(
                { synchronized(stateLock) { handleAction(it) } },
                { statesFlowableProcessor.onError(it) }
            )
        var disposable: Disposable? = null
        disposable = Completable
            .fromCallable {
                synchronized(stateLock) {
                    val state = state
                    initializers.forEach { it.apply { dispatcher.invoke(state) } }
                }
            }
            .let { completable -> scheduler?.let { completable.subscribeOn(it) } ?: completable }
            .subscribe(
                { disposable?.apply { disposables -= this } },
                {
                    statesFlowableProcessor.onError(it)
                    disposable?.apply { disposables -= this }
                }
            )
        if (!disposable.isDisposed) disposables += disposable
    }

}