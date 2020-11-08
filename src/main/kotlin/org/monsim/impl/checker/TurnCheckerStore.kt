package org.monsim.impl.checker

import org.monsim.bean.domain.Game
import org.monsim.bean.domain.Turn
import org.monsim.api.checker.ItemChecker
import org.monsim.bean.Range

class TurnCheckerStore {

    abstract class Turn_Abstract_Checker(private val game: Game) : AbstractCheckerStore.Abstract_SingleItem_Checker(game) {
        final override fun itemsToCheck(): List<Any> {
            return listOf(game.turn)
        }
        final override fun itemError(item: Any): String? {
            val turn = item as Turn
            var error = turnError(turn)
            if (error != null){
                error = "Bad turn '$turn': $error"
            }
            return error
        }
        abstract fun turnError(turn: Turn): String?
    }

    class Turn_PlayerId_Checker(val game: Game) : Turn_Abstract_Checker(game) {
        val playerIds = game.players.map{it.id}.toSet()
        override fun turnError(turn: Turn): String? {
            val playerId = turn.playerId
            return if (playerIds.contains(playerId)) null else
                "playerId '$playerId' doesn't exist"
        }
    }

    class Turn_DoublesRolled_Checker(val game: Game) : Turn_Abstract_Checker(game) {
        val maxDoubles = game.rule.maxDoubles
        override fun turnError(turn: Turn): String? {
            val doublesRolled = turn.doublesRolled
            return if (doublesRolled >= 0 && doublesRolled <= maxDoubles) null else
                "doublesRolled '$doublesRolled' isn't between 0 and '$maxDoubles'"
        }
    }

    class Turn_Roll_Checker(val game: Game) : Turn_Abstract_Checker(game) {
        val diceRange = Range(min = 2, max = 12)
        override fun turnError(turn: Turn): String? {
            val roll = turn.rollSum
            return if (roll >= diceRange.min && roll <= diceRange.max) null else
                "roll '$roll' isn't in diceRange $diceRange"
        }
    }

}