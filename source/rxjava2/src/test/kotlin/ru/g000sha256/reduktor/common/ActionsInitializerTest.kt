package ru.g000sha256.reduktor.common

import io.reactivex.processors.PublishProcessor
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.verifyNoMoreInteractions
import ru.g000sha256.reduktor.Dispatcher

class ActionsInitializerTest {

    @Test
    fun `invoke - dispatcher has one interaction too`() {
        // GIVEN
        val publishProcessor = PublishProcessor.create<Int>()
        val actionsInitializer = ActionsInitializer<Int, String>(publishProcessor)
        val dispatcher = mock<Dispatcher<Int>>()

        // WHEN
        actionsInitializer.apply { dispatcher.invoke("State") }

        // THEN
        verify(dispatcher)
            .apply {
                publishProcessor.launch(key = null)
            }
        verifyNoMoreInteractions(dispatcher)
    }

    @Test
    fun `onNext - dispatcher has one interaction too`() {
        // GIVEN
        val publishProcessor = PublishProcessor.create<Int>()
        val actionsInitializer = ActionsInitializer<Int, String>(publishProcessor)
        val dispatcher = mock<Dispatcher<Int>>()
        actionsInitializer.apply { dispatcher.invoke("State") }

        // WHEN
        publishProcessor.onNext(1)

        // THEN
        verify(dispatcher)
            .apply {
                publishProcessor.launch(key = null)
            }
        verifyNoMoreInteractions(dispatcher)
    }

}