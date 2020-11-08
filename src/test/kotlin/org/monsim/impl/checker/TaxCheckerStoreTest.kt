package org.monsim.impl.checker

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.monsim.bean.domain.Game
import org.monsim.bean.domain.Space
import org.monsim.bean.domain.Tax

class TaxCheckerStoreTest {


    @Test
    fun taxHasValidSpaceIdChecker() {
        val game = Game(spaces = listOf(Space(id = 1)))
        val checker = TaxCheckerStore.Tax_SpaceId_Checker(game)
        assertEquals(null, checker.taxError(Tax(spaceId = 1)))
        assertEquals("spaceId '2' doesn't exist", checker.taxError(Tax(spaceId = 2)))
    }

    @Test
    fun taxHasValidAmountItemChecker() {
        val game = Game()
        val checker = TaxCheckerStore.Tax_Amount_Checker(game)
        assertEquals(null, checker.taxError(Tax(amount = 1)))
        assertEquals(null, checker.taxError(Tax(amount = 0)))
        assertEquals("amount '-1' is negative", checker.taxError(Tax(amount = -1)))
    }
}

