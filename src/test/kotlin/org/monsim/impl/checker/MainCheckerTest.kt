package org.monsim.impl.checker

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.monsim.bean.domain.Game
import org.monsim.bean.domain.Space
import org.monsim.bean.domain.Tax

class MainCheckerTest {


    val mainChecker = MainCheckerImpl()

    @Test
    fun gameErrors1() {
        val game = Game()
        val itemCheckers = mainChecker.itemCheckers(game).filter{it is TaxCheckerStore.Tax_Abstract_Checker}.toList()
        val errors = mainChecker.gameErrorsForItemCheckers(game, itemCheckers)
        assertEquals(0, errors.size)
    }

    @Test
    fun gameErrors2() {
        val game = Game(taxes=listOf(Tax(spaceId = 1, amount = -2)), spaces = listOf(Space(id=1)))
        val itemCheckers = mainChecker.itemCheckers(game).filter{it is TaxCheckerStore.Tax_Abstract_Checker}.toList()
        val errors = mainChecker.gameErrorsForItemCheckers(game, itemCheckers)
        assertEquals(listOf("Bad tax 'Tax(spaceId=1, amount=-2)': amount '-2' is negative"), errors)
    }

    @Test
    fun all(){
        val game = Game()
        val itemCheckers = mainChecker.itemCheckers(game).toList()
        val errors = mainChecker.gameErrorsForItemCheckers(game, itemCheckers)
        assertEquals(listOf<String>(), errors)
    }

}

