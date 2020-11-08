package org.monsim.impl.checker

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.monsim.bean.domain.Game
import org.monsim.bean.domain.Space
import org.monsim.bean.domain.Pile

class PileCheckerStoreTest {


    @Test
    fun pileHasValidSpaceIdChecker() {
        val game = Game(
                spaces = listOf(Space(id = 11)),
                piles = listOf(
                        Pile(id = 1, spaceIds = setOf(11)),
                        Pile(id = 2, spaceIds = setOf(12))
                )
        )
        val checker = PileCheckerStore.Pile_SpaceIds_Checker(game)
        assertEquals(null, checker.pileError(game.piles[0]))
        assertEquals("invalid spaceIds '[12]'",
                checker.pileError(game.piles[1]))
    }


}

