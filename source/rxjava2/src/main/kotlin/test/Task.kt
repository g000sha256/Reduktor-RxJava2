package test

interface Task {

    val isCompleted: Boolean

    fun cancel()

    fun start()

}