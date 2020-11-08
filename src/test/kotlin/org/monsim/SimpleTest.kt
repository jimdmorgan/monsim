package org.monsim.org.monsim

import mu.KotlinLogging
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class SimpleTest {

    private val log = KotlinLogging.logger {}


    @Test
    fun logOutput() {

        log.info {"This is a test"}
        Assertions.assertEquals(4, 4)
    }
}