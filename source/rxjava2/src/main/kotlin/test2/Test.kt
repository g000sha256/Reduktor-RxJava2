package test2

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import test.SideEffect
import test.Task

fun SideEffect.Context<String, String>.test(coroutineScope: CoroutineScope) {
    taskCreator += task { coroutineScope.launch { } }
    taskCreator["2"] = task { coroutineScope.launch { } }
}

fun task(creator: () -> Job): () -> Task {
    return {
        val job = creator()
        object : Task {

            override val isCompleted: Boolean
                get() = !job.isActive

            override fun cancel() {
                job.cancel()
            }

        }
    }
}