package ru.g000sha256.reduktor.rxjava2

import io.reactivex.Flowable
import org.junit.Assert
import org.junit.Test

class ExtTest {

    @Test
    fun `instanceOf - positive result`() {
        // GIVEN
        val value = "text"
        val clazz = String::class.java

        // WHEN
        val result = value instanceOf clazz

        // THEN
        Assert.assertTrue(result)
    }

    @Test
    fun `instanceOf - negative result`() {
        // GIVEN
        val value = "text"
        val clazz = Int::class.java

        // WHEN
        val result = value instanceOf clazz

        // THEN
        Assert.assertFalse(result)
    }

    @Test
    fun toFlowable() {
        // GIVEN
        val list = listOf(
            Flowable.just(1),
            Flowable.just(2),
            Flowable.just(3)
        )

        // WHEN
        val flowable = list.toFlowable()

        // THEN
        val testSubscriber = flowable.test()
        testSubscriber.assertValues(1, 2, 3)
    }

}