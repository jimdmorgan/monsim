package org.monsim.impl.util

import mu.KotlinLogging
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.monsim.api.util.Spacer
import org.monsim.bean.SpaceMove
import org.monsim.bean.domain.Game
import org.monsim.bean.domain.Group
import org.monsim.bean.domain.Player
import org.monsim.bean.domain.Property
import org.monsim.bean.domain.Rule
import org.monsim.bean.domain.Space
import org.monsim.bean.domain.Turn


class SpacerImplTest {
    private val log = KotlinLogging.logger {}
    private val spacer: Spacer = SpacerImpl()

    @Test
    fun calcSpaceMove(){
        val spaces = listOf(
                Space(id = 101),
                Space(id = 102),
                Space(id = 103),
                Space(id = 104),
                Space(id = 105),
                Space(id = 106),
                Space(id = 107),
                Space(id = 108),
        )
        val player = Player(
                id = 51
        )
        val game = Game(
                spaces = spaces,
                players = listOf(player),
                turn = Turn(playerId = 51),
                rule = Rule(goSpaceId = 101)
        )

        player.spaceId = 101
        assertEquals(SpaceMove(newSpace = Space(id = 108), isGoPassed = false), spacer.calcSpaceMove(7, game))
        assertEquals(SpaceMove(newSpace = Space(id = 101), isGoPassed = true), spacer.calcSpaceMove(8, game))
        assertEquals(SpaceMove(newSpace = Space(id = 102), isGoPassed = true), spacer.calcSpaceMove(9, game))
        player.spaceId = 102
        assertEquals(SpaceMove(newSpace = Space(id = 108), isGoPassed = false), spacer.calcSpaceMove(6, game))
        assertEquals(SpaceMove(newSpace = Space(id = 101), isGoPassed = true), spacer.calcSpaceMove(7, game))
        assertEquals(SpaceMove(newSpace = Space(id = 102), isGoPassed = true), spacer.calcSpaceMove(8, game))
    }

    @Test
    fun spaceOfNearestPropertyInGroup() {
        val spaces = listOf(
                Space(id = 101),
                Space(id = 102),
                Space(id = 103),
                Space(id = 104),
                Space(id = 105),
                Space(id = 106),
                Space(id = 107),
                Space(id = 108),
        )
        val properties = listOf(
                Property(id = 1, spaceId = 102, groupId = 8),
                Property(id = 2, spaceId = 104, groupId = 9),
                Property(id = 3, spaceId = 106, groupId = 8)
        )
        val player = Player(
                id = 51
        )
        val game = Game(
                spaces = spaces,
                properties = properties,
                players = listOf(player),
                turn = Turn(playerId = 51)
        )
        player.spaceId = 101
        assertEquals(Space(id = 102), spacer.spaceOfNearestPropertyInGroup(Group(id = 8), game))
        assertEquals(Space(id = 104), spacer.spaceOfNearestPropertyInGroup(Group(id = 9), game))
        player.spaceId = 104
        assertEquals(Space(id = 106), spacer.spaceOfNearestPropertyInGroup(Group(id = 8), game))
        assertEquals(Space(id = 104), spacer.spaceOfNearestPropertyInGroup(Group(id = 9), game))
    }

    @Test
    fun rollNeededToGetToSpace(){
        val spaces = listOf(
                Space(id = 101),
                Space(id = 102),
                Space(id = 103),
                Space(id = 104),
                Space(id = 105),
                Space(id = 106),
                Space(id = 107),
                Space(id = 108),
        )
        val player = Player(
                id = 51
        )
        val game = Game(
                spaces = spaces,
                players = listOf(player),
                turn = Turn(playerId = 51)
        )

        player.spaceId = 101
        assertEquals(3, spacer.rollNeededToGetToSpace(Space(id = 104), game))
        player.spaceId = 107
        assertEquals(2, spacer.rollNeededToGetToSpace(Space(id = 101), game))
    }



}