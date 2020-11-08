package org.monsim.impl.play

import mu.KotlinLogging
import org.monsim.api.util.GameUtil
import org.monsim.api.play.Roller
import org.monsim.bean.DiceRoll
import org.monsim.bean.RollLogic
import org.monsim.bean.domain.Game
import org.monsim.bean.domain.Player
import org.monsim.bean.type.OutOfJailWay
import org.monsim.impl.util.GameUtilImpl
import org.monsim.impl.util.RandomDiceImpl
import java.lang.RuntimeException


class RollerImpl() : Roller {

    private val log = KotlinLogging.logger {}
    private val gameUtil: GameUtil = GameUtilImpl()
    private val jailer = JailerImpl()
    private val mover = MoverImpl()
    private val diceRoller = RandomDiceImpl()

    inner class Delegate(val game: Game) {

        fun roll() {
            val diceRoll = diceRoller.randomRoll(game.rule.seed, game.turn.totalRolls)
            val player = gameUtil.currentPlayer(game)
            log.debug{"player#${player.id} rolled: ${diceRoll}"}
            val rollLogic = rollLogic(diceRoll)
            applyRollLogic(rollLogic)
        }

        fun applyRollLogic(rollLogic: RollLogic) {
            val player = currentPlayer()
            log.debug{"applying rollLogic for player#${player.id}: ${rollLogic}"}

            val turn = game.turn
            turn.rollSum = rollLogic.rollSum
            game.turn.totalRolls++

            if (rollLogic.incrementRollsInJail) incrementRollsInJail()
            if (rollLogic.incrementDoublesRolled) incrementDoublesRolled()
            if (rollLogic.isRollingDone) turn.isRollingDone = true
            if (rollLogic.isFreed) jailer.free(OutOfJailWay.ROLLED_DOUBLES, game)
            if (rollLogic.isJailed) jailer.jail(game)
            if (rollLogic.isMoved) mover.move(rollLogic.rollSum, game)
        }



        private fun incrementDoublesRolled() {
            val player = currentPlayer()
            val turn = game.turn
            turn.doublesRolled++
            log.debug{"player#${player.id} rolled doubles. doubleRolledThisTurn=${turn.doublesRolled}"}
        }

        private fun incrementRollsInJail() {
            val player = currentPlayer()
            player.rollsInJail++
            log.debug{"Player#${player.id} stays in jail after not rolling doubles rollsInJail:${player.rollsInJail} "}
        }


        fun rollLogic(diceRoll: DiceRoll): RollLogic {
            val reasonWhyPlayerCantRoll = reasonWhyPlayerCantRoll()
            when {
                reasonWhyPlayerCantRoll != null -> throw RuntimeException("player can't roll: ${reasonWhyPlayerCantRoll}")
            }
            val player = currentPlayer()
            val isDoubles = diceRoll.isDoubles
            val isJailed = player.jailed
            val diceSum = diceRoll.sum
            val rollLogic = when {
                isJailed && isDoubles -> RollLogic(isRollingDone = true, isFreed = true, isMoved = true)
                isJailed && !isDoubles -> RollLogic(isRollingDone = true, incrementRollsInJail = true)
                !isJailed && !isDoubles -> RollLogic(isRollingDone = true, isMoved = true)
                !isJailed && isDoubles -> handleFreeAndDoubles()
                else -> throw RuntimeException("logic error on isJailed/isDoubles")
            }
            rollLogic.rollSum = diceSum
            return rollLogic

        }

        private fun handleFreeAndDoubles(): RollLogic {
            val doublesRolled = game.turn.doublesRolled
            val maxDoublesAllowed = game.rule.maxDoubles
            val isGoingToJail = (doublesRolled + 1) > maxDoublesAllowed
            val rollLogic = RollLogic(
                    isJailed = isGoingToJail,
                    isMoved = !isGoingToJail,
                    isRollingDone = isGoingToJail,
                    incrementDoublesRolled = true
            )
            return rollLogic
        }

        fun mayRoll(): Boolean {
            val reasonPlayerCantRoll = reasonWhyPlayerCantRoll()
            val mayRoll = reasonPlayerCantRoll == null
            val player = gameUtil.currentPlayer(game)
            val explaination = if (mayRoll) "may roll" else "can't roll because $reasonPlayerCantRoll"
            log.debug { "player#${player.id} $explaination" }
            return mayRoll
        }

        private fun reasonWhyPlayerCantRoll(): String? {
            val player = currentPlayer()
            val reason = when {
                gameUtil.isGameOver(game) -> "game over"
                game.turn.isRollingDone -> "player already rolled"
                player.cash < 0 -> "player is in debt"
                else -> null
            }
            return reason
        }

        private fun currentPlayer(): Player {
            val currentPlayer = gameUtil.currentPlayer(game)
            return currentPlayer
        }

    }

    override fun applyRollLogic(rollLogic: RollLogic, game: Game) {
        val delegate = Delegate(game)
        return delegate.applyRollLogic(rollLogic)
    }

    override fun rollLogic(diceRoll: DiceRoll, game: Game): RollLogic {
        val delegate = Delegate(game)
        return delegate.rollLogic(diceRoll)
    }

    override fun mayRoll(game: Game): Boolean {
        val delegate = Delegate(game)
        return delegate.mayRoll()
    }

    override fun roll(game: Game) {
        val delgate = Delegate(game)
        return delgate.roll()
    }
}