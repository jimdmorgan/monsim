package org.monsim.impl.play

import mu.KotlinLogging
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.monsim.api.play.Mover
import org.monsim.bean.domain.Game
import org.monsim.impl.util.GameUtilImpl

class MoverTest {
    private val log = KotlinLogging.logger {}
    private val mover: Mover = MoverImpl()
    private val gameUtil = GameUtilImpl()

    @Test
    fun move1() {
        val game: Game = Game()
        mover.move(4, game)
        assertEquals(1300, game.players[0].cash)
    }

    @Test
    fun move2() {
        val game: Game = Game()
        game.properties[0].ownerId = game.players[1].id
        mover.move(1, game)
        assertEquals(1498, game.players[0].cash)
        assertEquals(1502, game.players[1].cash)
    }

    @Test
    fun move3(){
        val game: Game = Game()
        gameUtil.cardForId(902003, game).timesUsed = -1
        mover.move(2, game)
        assertEquals(1450, game.players[0].cash)
    }

    @Test
    fun move4(){
        val game: Game = Game()
        gameUtil.cardForId(902006, game).timesUsed = -1
        mover.move(2, game)
        assertEquals(true, game.players[0].jailed)
        assertEquals(game.rule.jailSpaceId, game.players[0].spaceId)
        assertEquals(1500, game.players[0].cash)
    }





}