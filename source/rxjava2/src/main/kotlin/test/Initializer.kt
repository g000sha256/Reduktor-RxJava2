package test

import io.reactivex.disposables.Disposable

fun interface Initializer<A, S> {

    fun Context<A>.invoke(initialState: S)

    interface Context<A> {

        val actions: Actions<A>
        val disposables: Disposables

        interface Disposables {

            operator fun plusAssign(disposable: Disposable)

            operator fun set(key: String, disposable: Disposable)

        }

    }

}