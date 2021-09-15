package ru.g000sha256.reduktor.rxjava2.common

import io.reactivex.processors.PublishProcessor
import org.junit.Assert
import org.junit.Test

class ActionsInitializerTest {

    @Test
    fun `invoke - input flowable and output flowable are the same`() {
        // GIVEN
        val publishProcessor = PublishProcessor.create<Int>()
        val actionsInitializer = ActionsInitializer<Int, Int>(publishProcessor)

        // WHEN
        val flowable = actionsInitializer.invoke(0)

        // THEN
        Assert.assertEquals(publishProcessor, flowable)
    }

}