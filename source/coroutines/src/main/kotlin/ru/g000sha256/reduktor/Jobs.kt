package ru.g000sha256.reduktor

import kotlinx.coroutines.Job

class Jobs {

    private val lock = Any()
    private val jobsMutableList = mutableListOf<Job>()
    private val jobsMutableMap = mutableMapOf<String, Job>()

    fun add(job: Job) {
        synchronized(lock) {
            remove(job)
            jobsMutableList += job
        }
    }

    fun add(key: String, job: Job) {
        synchronized(lock) {
            remove(key)
            jobsMutableMap[key] = job
        }
    }

    fun clear() {
        synchronized(lock) {
            jobsMutableList.forEach { it.cancel() }
            jobsMutableList.clear()
            jobsMutableMap.forEach { it.value.cancel() }
            jobsMutableMap.clear()
        }
    }

    fun remove(job: Job) {
        synchronized(lock) {
            val isDeleted = jobsMutableList.remove(job)
            if (isDeleted) {
                job.cancel()
            } else {
                val entry = jobsMutableMap.entries.find { it.value == job } ?: return@synchronized
                jobsMutableMap.remove(entry.key)
                job.cancel()
            }
        }
    }

    fun remove(key: String) {
        synchronized(lock) {
            val job = jobsMutableMap.remove(key)
            job?.cancel()
        }
    }

    operator fun minusAssign(job: Job) {
        remove(job)
    }

    operator fun plusAssign(job: Job) {
        add(job)
    }

    operator fun set(key: String, job: Job) {
        add(key, job)
    }

}