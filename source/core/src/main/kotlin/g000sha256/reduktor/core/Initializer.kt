package g000sha256.reduktor.core

fun interface Initializer<A, S> {

    fun Environment<A>.invoke(initialState: S)

}