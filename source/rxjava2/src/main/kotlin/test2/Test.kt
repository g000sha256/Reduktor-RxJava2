package test2

import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

fun SideEffect.Context<String, String>.test() {
    taskCreator += TaskCreator.Creator { delay(100) }
    taskCreator["2"] = TaskCreator.Creator { launch {} }
}