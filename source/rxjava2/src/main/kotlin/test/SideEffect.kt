package test

interface SideEffect<A, S> {

    fun Dispatcher<A>.onInit(initialState: S) {}

    fun Dispatcher<A>.onNewAction(action: A, currentState: S) {}

    fun onCleared() {}

}