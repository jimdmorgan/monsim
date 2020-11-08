package org.monsim.impl.play

import mu.KotlinLogging
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.monsim.bean.domain.Game
import org.monsim.bean.domain.Group
import org.monsim.bean.domain.Player
import org.monsim.bean.domain.Property
import org.monsim.bean.domain.Rule
import org.monsim.bean.domain.Space

class MortgagerTest {
    private val log = KotlinLogging.logger {}
    private val mortgager = MortgagerImpl()


    @Test
    fun mortgage() {
        val player = Player(id = 1, cash = 500)
        val property = Property(id = 101, groupId = 9, ownerId = 1, mortgaged = false)
        val game = Game(
                spaces = listOf(Space(id = 0)),
                groups = listOf(Group(id = 9, buyPrices=listOf(180))),
                players = listOf(player),
                rule = Rule(mortgagePercent = 25)
        )
        mortgager.mortgage(property, game)
        assertEquals(Property(id = 101, groupId = 9, ownerId = 1, mortgaged = true), property)
        assertEquals(Player(id = 1, cash = 545), player)
    }

    @Test
    fun unmortgage() {
        val player = Player(id = 1, cash = 500)
        val property = Property(id = 101, groupId = 9, ownerId = 1, mortgaged = false)
        val game = Game(
                spaces = listOf(Space(id = 0)),
                groups = listOf(Group(id = 9, buyPrices=listOf(180))),
                players = listOf(player),
                rule = Rule(mortgagePercent = 25)
        )
        mortgager.mortgage(property, game)
        assertEquals(Property(id = 101, groupId = 9, ownerId = 1, mortgaged = true), property)
        assertEquals(Player(id = 1, cash = 545), player)
    }


}