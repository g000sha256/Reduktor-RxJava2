package ru.g000sha256.reduktor

import io.reactivex.rxjava3.disposables.Disposable

class Disposables {

    private val lock = Any()
    private val disposablesList = mutableListOf<Disposable>()
    private val disposablesMap = mutableMapOf<String, Disposable>()

    fun add(disposable: Disposable) {
        synchronized(lock) { disposablesList += disposable }
    }

    operator fun plusAssign(disposable: Disposable) {
        add(disposable)
    }

    fun remove(disposable: Disposable) {
        synchronized(lock) {
            disposablesList -= disposable
            disposable.dispose()
        }
    }

    operator fun minusAssign(disposable: Disposable) {
        remove(disposable)
    }

    fun add(key: String, disposable: Disposable) {
        synchronized(lock) { disposablesMap[key] = disposable }
    }

    operator fun set(key: String, disposable: Disposable) {
        add(key, disposable)
    }

    fun remove(key: String) {
        synchronized(lock) {
            val disposable = disposablesMap.remove(key)
            disposable?.dispose()
        }
    }

    fun clear() {
        synchronized(lock) {
            disposablesList.forEach { it.dispose() }
            disposablesList.clear()
            disposablesMap.forEach { it.value.dispose() }
            disposablesMap.clear()
        }
    }

}