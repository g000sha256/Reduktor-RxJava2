package test

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job

fun interface Initializer<A, S> {

    fun Context<A>.invoke(initialState: S)

    interface Context<A> {

        val actions: Actions<A>
        val scope: CoroutineScope
        val jobs: Jobs

        interface Jobs {

            operator fun plusAssign(job: Job)

            operator fun set(key: String, job: Job)

        }

    }

}