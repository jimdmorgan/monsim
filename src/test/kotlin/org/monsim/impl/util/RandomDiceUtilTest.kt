package org.monsim.impl.util

import mu.KotlinLogging
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.monsim.api.util.RandomDiceUtil

class RandomDiceUtilTest {
    private val log = KotlinLogging.logger {}
    private val randomDiceUtil:RandomDiceUtil = RandomDiceImpl()


    @Test
    fun randomRolls() {
        for (x in 0L..10L){
            val diceRoll = randomDiceUtil.randomRoll(12345L, x)
            log.debug{"diceRoll:$diceRoll"}
            assertTrue(diceRoll.sum >= 2 && diceRoll.sum <= 12, "bad diceRoll: $diceRoll")
            assertTrue( !(diceRoll.isDoubles && diceRoll.sum % 2 == 1), "bad doubles diceRoll: $diceRoll")
        }
    }


}