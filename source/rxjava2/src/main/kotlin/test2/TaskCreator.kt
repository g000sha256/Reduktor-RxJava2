package test2

import kotlinx.coroutines.CoroutineScope

interface TaskCreator {

    operator fun plusAssign(creator: Creator)

    operator fun set(key: String, creator: Creator)

    fun interface Creator {

        suspend fun CoroutineScope.invoke()

    }

}