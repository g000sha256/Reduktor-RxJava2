package ru.g000sha256.reduktor.common

import org.junit.Test

class NewsSideEffectTest {

    /*@Test
    fun `subscribe - returns empty flowable`() {
        // GIVEN
        val newsSideEffect = NewsSideEffectByClass<Int, Int, Int>()
        val invokeFlowable = newsSideEffect.invoke(1, 2)

        // WHEN
        val testSubscriber = invokeFlowable.test()

        // THEN
        testSubscriber.assertComplete()
        testSubscriber.assertNoErrors()
        testSubscriber.assertNoValues()
    }

    @Test
    fun `invoke - filters by type - positive result`() {
        // GIVEN
        val newsSideEffect = NewsSideEffectByClass<Int, Int, Int>()
        val testSubscriber = newsSideEffect.news.test()

        // WHEN
        newsSideEffect.invoke(1, 2)

        // THEN
        testSubscriber.assertNotComplete()
        testSubscriber.assertNoErrors()
        testSubscriber.assertValues(1)
    }

    @Test
    fun `invoke - filters by type - negative result`() {
        // GIVEN
        val newsSideEffect = NewsSideEffectByClass<Int, Int, Boolean>()
        val testSubscriber = newsSideEffect.news.test()

        // WHEN
        newsSideEffect.invoke(1, 2)

        // THEN
        testSubscriber.assertNotComplete()
        testSubscriber.assertNoErrors()
        testSubscriber.assertNoValues()
    }

    @Test
    fun `invoke - filters by action-state callback - positive result`() {
        // GIVEN
        val newsSideEffect = NewsSideEffect.FromCallback<Int, Int, String> { action, state -> "${action + state}" }
        val testSubscriber = newsSideEffect.news.test()

        // WHEN
        newsSideEffect.invoke(1, 2)

        // THEN
        testSubscriber.assertNotComplete()
        testSubscriber.assertNoErrors()
        testSubscriber.assertValues("3")
    }

    @Test
    fun `invoke - filters by action-state callback - negative result`() {
        // GIVEN
        val newsSideEffect = NewsSideEffect.FromCallback<Int, Int, String> { _, _ -> null }
        val testSubscriber = newsSideEffect.news.test()

        // WHEN
        newsSideEffect.invoke(1, 2)

        // THEN
        testSubscriber.assertNotComplete()
        testSubscriber.assertNoErrors()
        testSubscriber.assertNoValues()
    }*/

}