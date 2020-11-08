package org.monsim.impl.checker

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.monsim.bean.domain.Game
import org.monsim.bean.domain.Rule
import org.monsim.bean.domain.Space

class SpaceCheckerStoreTest {


    @Test
    fun Space_Type_Checker() {
        val game = Game(
                spaces = listOf(Space(id = 11), Space(id = 12), Space(id = 13)),
                rule = Rule(freeSpaceIds = setOf(11, 13), goSpaceId = 13)
        )
        val checker = SpaceCheckerStore.Space_Type_Checker(game)
        assertEquals(null, checker.spaceError(game.spaces[0]))
        assertEquals(null, checker.spaceError(game.spaces[1]))
        assertEquals("has multiple spaceTypes ([FREE, GO])",
                checker.spaceError(game.spaces[2]))
    }

    @Test
    fun Space_IdsUnique_Checker() {
        val game = Game(spaces = listOf(
                Space(id = 11), Space(id = 12), Space(id = 11)))
        val checker = SpaceCheckerStore.Space_IdsUnique_Checker(game)
        assertEquals(null, checker.spaceError(game.spaces[1]))
        assertEquals("duplicate id '11'",
                checker.spaceError(game.spaces[0]))
    }

    @Test
    fun Space_NamesUnique_Checker() {
        val game = Game(spaces = listOf(
                Space(id = 1, name = "a"), Space(id = 2, name = "b"), Space(id = 3, name = "a")))
        val checker = SpaceCheckerStore.Space_NameUnique_Checker(game)
        assertEquals(null, checker.spaceError(game.spaces[1]))
        assertEquals("duplicate name 'a'",
                checker.spaceError(game.spaces[0]))
    }



}

