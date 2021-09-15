package ru.g000sha256.reduktor

import kotlinx.coroutines.Job

class Jobs {

    private val lock = Any()
    private val jobsList = mutableListOf<Job>()
    private val jobsMap = mutableMapOf<String, Job>()

    fun add(job: Job) {
        synchronized(lock) { jobsList += job }
    }

    operator fun plusAssign(job: Job) {
        add(job)
    }

    fun remove(job: Job) {
        synchronized(lock) {
            jobsList -= job
            job.cancel()
        }
    }

    operator fun minusAssign(job: Job) {
        remove(job)
    }

    fun add(key: String, job: Job) {
        synchronized(lock) { jobsMap[key] = job }
    }

    operator fun set(key: String, job: Job) {
        add(key, job)
    }

    fun remove(key: String) {
        synchronized(lock) {
            val job = jobsMap.remove(key)
            job?.cancel()
        }
    }

    fun clear() {
        synchronized(lock) {
            jobsList.forEach { it.cancel() }
            jobsList.clear()
            jobsMap.forEach { it.value.cancel() }
            jobsMap.clear()
        }
    }

}