package test

interface TaskCreator {

    operator fun plusAssign(creator: () -> Task)

    operator fun set(key: String, creator: () -> Task)

}