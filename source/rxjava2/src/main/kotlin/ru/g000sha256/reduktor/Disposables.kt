package ru.g000sha256.reduktor

import io.reactivex.disposables.Disposable

class Disposables {

    private val lock = Any()
    private val disposablesMutableList = mutableListOf<Disposable>()
    private val disposablesMutableMap = mutableMapOf<String, Disposable>()

    fun add(disposable: Disposable) {
        synchronized(lock) {
            remove(disposable)
            disposablesMutableList += disposable
        }
    }

    fun add(key: String, disposable: Disposable) {
        synchronized(lock) {
            remove(key)
            disposablesMutableMap[key] = disposable
        }
    }

    fun clear() {
        synchronized(lock) {
            disposablesMutableList.forEach { it.dispose() }
            disposablesMutableList.clear()
            disposablesMutableMap.forEach { it.value.dispose() }
            disposablesMutableMap.clear()
        }
    }

    fun remove(disposable: Disposable) {
        synchronized(lock) {
            val isDeleted = disposablesMutableList.remove(disposable)
            if (isDeleted) {
                disposable.dispose()
            } else {
                val entry = disposablesMutableMap.entries.find { it.value == disposable } ?: return@synchronized
                disposablesMutableMap.remove(entry.key)
                disposable.dispose()
            }
        }
    }

    fun remove(key: String) {
        synchronized(lock) {
            val disposable = disposablesMutableMap.remove(key)
            disposable?.dispose()
        }
    }

    operator fun minusAssign(disposable: Disposable) {
        remove(disposable)
    }

    operator fun plusAssign(disposable: Disposable) {
        add(disposable)
    }

    operator fun set(key: String, disposable: Disposable) {
        add(key, disposable)
    }

}