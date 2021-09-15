package ru.g000sha256.reduktor.rxjava2

import org.junit.Test

class StoreTest {

    @Test
    fun `test 1`() {
        // GIVEN
        val store = Store<String, String>(
            state = "State",
            reducer = { action, state -> action + state }
        )

        // WHEN
        val testSubscriber = store.states.test()

        // THEN
        testSubscriber.assertNotComplete()
        testSubscriber.assertNoErrors()
        testSubscriber.assertValues("State")
    }

}