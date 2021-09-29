package test

import io.reactivex.disposables.Disposable

fun interface SideEffect<A, S> {

    fun Context<A>.invoke(action: A, state: S)

    interface Context<A> {

        val actions: Actions<A>
        val disposables: Disposables

        interface Disposables {

            fun cancel(key: String)

            operator fun plusAssign(disposable: Disposable)

            operator fun set(key: String, disposable: Disposable)

        }

    }

}