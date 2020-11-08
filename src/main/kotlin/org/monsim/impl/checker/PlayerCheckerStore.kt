package org.monsim.impl.checker

import org.monsim.bean.Constants
import org.monsim.bean.domain.Game
import org.monsim.bean.domain.Player
import org.monsim.impl.util.UniqueUtilImpl

class PlayerCheckerStore {


    abstract class Player_Abstract_Checker(private val game: Game) : AbstractCheckerStore.Abstract_Checker(game) {
        val playerIds = game.players.map{it.id}.toSet()
        val uniqueUtil = UniqueUtilImpl()
        final override fun itemsToCheck(): List<Any> {
            return game.players
        }
    }

    abstract class Player_SingleAbstract_Checker(private val game: Game) : Player_Abstract_Checker(game) {

        final override fun itemError(item: Any): String? {
            val player = item as Player
            var error = playerError(player)
            if (error != null){
                error = "Bad Player#${player.id}: $error"
            }
            return error
        }
        abstract fun playerError(player: Player): String?
    }

    class Player_Aggregate_OneOrMore_Checker(val game: Game) : Player_Abstract_Checker(game) {
        override fun aggregateError(): String? {
            val error = if (game.players.size > 0) null else "no players"
            return error
        }
    }

    class Player_Aggregate_AtLeastOnePlaying_Checker(val game: Game) : Player_Abstract_Checker(game) {
        override fun aggregateError(): String? {
            val playersInGame = game.players.filter{!it.lost}.toSet()
            val error = if (playersInGame.size > 0) null else "no players in game who haven't lost"
            return error
        }
    }



    class Player_RollsInJail_Checker(val game: Game) : Player_SingleAbstract_Checker(game) {
        private val maxRolls = game.rule.maxRollsInJail

        override fun playerError(player: Player): String? {
            val rollsInJail = player.rollsInJail
            val error = if (rollsInJail >= 0 && rollsInJail <= maxRolls) null else
                "maxRolls $rollsInJail' isn't between 0 and '$maxRolls'"
            return error
        }
    }

    class Player_CreditorId_Checker(val game: Game) : Player_SingleAbstract_Checker(game) {
        private val validPlayerIds = game.players.map{it.id}.toSet()

        override fun playerError(player: Player): String? {
            val creditorId = player.creditorId
            val error = if (creditorId == Constants.BANK_ID || validPlayerIds.contains(creditorId)) null else
                "invalid creditorId '$creditorId'"
            return error
        }
    }

    class Player_FreeBailCards_Checker(val game: Game) : Player_SingleAbstract_Checker(game) {
        override fun playerError(player: Player): String? {
            val freeBailCards = player.freeBailCards
            val error = if (freeBailCards >= 0) null else
                "negative freeBailCards '$freeBailCards'"
            return error
        }
    }

    class Player_SpaceId_Checker(val game: Game) : Player_SingleAbstract_Checker(game) {
        val validSpaceIds = game.spaces.map{it.id}.toSet()
        override fun playerError(player: Player): String? {
            val spaceId = player.spaceId
            val error = if (validSpaceIds.contains(spaceId)) null else
                "invalid spaceId '$spaceId'"
            return error
        }
    }


    class Player_IdsUnique_Checker(val game: Game) : Player_SingleAbstract_Checker(game) {
        val repeatedPlayerIds = uniqueUtil.repeatedInts(
                game.players.map{it.id}.toList())
        override fun playerError(player: Player): String? {
            val playerId = player.id
            val error = if (!repeatedPlayerIds.contains(playerId)) null else
                "duplicate id '$playerId'"
            return error
        }
    }

}