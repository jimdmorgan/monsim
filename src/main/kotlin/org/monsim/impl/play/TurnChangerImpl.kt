package org.monsim.impl.play

import mu.KotlinLogging
import org.monsim.api.play.TurnChanger
import org.monsim.api.util.GameUtil
import org.monsim.bean.domain.Game
import org.monsim.bean.domain.Player
import org.monsim.impl.util.GameUtilImpl
import org.monsim.impl.util.StringUtilImpl
import java.lang.RuntimeException

class TurnChangerImpl() : TurnChanger {

    private val log = KotlinLogging.logger {}
    private val gameUtil: GameUtil = GameUtilImpl()
    private val stringUtil = StringUtilImpl()



    override fun endTurn(game: Game) {
        val reasonWhyCantEndTurn = reasonWhyCantEndTurn(game)
        when{
            reasonWhyCantEndTurn != null -> throw RuntimeException("can't end turn: ${reasonWhyCantEndTurn}")
        }
        val lastPlayer = gameUtil.currentPlayer(game)
        val nextPlayer: Player = nextPlayer(game)
        val turn = game.turn
        turn.isRollingDone = false
        turn.doublesRolled = 0
        turn.playerId = nextPlayer.id
        turn.rollSum = 0
        log.debug{"Changing turn from Player#${lastPlayer.id} to Player#${nextPlayer.id}" }
    }

    private fun nextPlayer(game: Game): Player {
        val currentPlayer = gameUtil.currentPlayer(game)
        val players = game.players.filter{!it.lost || it == currentPlayer}.toList()
        val currentIndex = players.indexOf(currentPlayer)
        val nextIndex = (currentIndex + 1) % players.size
        val nextPlayer = players[nextIndex]
        return nextPlayer
    }

    override fun mayEndTurn(game: Game): Boolean {
        val reasonWhyCantEndTurn = reasonWhyCantEndTurn(game)
        val mayEndTurn = reasonWhyCantEndTurn == null
        val player = gameUtil.currentPlayer(game)
        log.debug{"Player#${player.id} ${if (mayEndTurn) "can" else "can't"} end turn ${if (mayEndTurn) "" else "because ${reasonWhyCantEndTurn}" }"}
        return mayEndTurn
    }

    fun reasonWhyCantEndTurn(game: Game): String? {
        val turn = game.turn
        val player = gameUtil.currentPlayer(game)
        if (player.lost){
            return null
        }
        val inDebt = player.cash < 0
        val exceededRollsInJail = player.rollsInJail >= game.rule.maxRollsInJail
        val gameOver = gameUtil.isGameOver(game)
        val reason = when{
            inDebt -> "player is in debt (cash=$${player.cash}) to ${stringUtil.player(gameUtil.playerForId(player.creditorId, game))}"
            exceededRollsInJail -> "player exceeded max attempts to roll doubles in jail: ${player.rollsInJail} max=${game.rule.maxRollsInJail}"
            !turn.isRollingDone -> "player needs to roll"
            gameOver -> "game is over"
            else -> null
        }
        return reason

    }


}