package test

infix fun SideEffect.Context.Disposables.cancel(key: String) {
    cancel(key)
}