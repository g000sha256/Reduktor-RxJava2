package test

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job

fun interface SideEffect<A, S> {

    fun Context<A>.invoke(action: A, state: S)

    interface Context<A> {

        val actions: Actions<A>
        val scope: CoroutineScope
        val jobs: Jobs

        interface Jobs {

            fun cancel(key: String)

            operator fun plusAssign(job: Job)

            operator fun set(key: String, job: Job)

        }

    }

}