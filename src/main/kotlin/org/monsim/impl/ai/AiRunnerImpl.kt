package org.monsim.impl.ai

import mu.KotlinLogging
import org.monsim.api.ai.AiRunner
import org.monsim.api.play.HouseBuilder
import org.monsim.bean.domain.Game
import org.monsim.bean.domain.Player
import org.monsim.bean.domain.Property
import org.monsim.bean.type.OutOfJailWay
import org.monsim.impl.checker.MainCheckerImpl
import org.monsim.impl.play.HouseBuilderImpl
import org.monsim.impl.play.JailerImpl
import org.monsim.impl.play.MortgagerImpl
import org.monsim.impl.play.PropertyBuyerImpl
import org.monsim.impl.play.ResignerImpl
import org.monsim.impl.play.RollerImpl
import org.monsim.impl.play.TurnChangerImpl
import org.monsim.impl.util.GameUtilImpl
import org.monsim.impl.util.MonopolyUtilImpl
import org.monsim.impl.util.NetWorthCalculatorImpl
import org.monsim.impl.util.PricerImpl
import java.lang.RuntimeException


class AiRunnerImpl() : AiRunner {

    private val log = KotlinLogging.logger {}
    private val gameUtil = GameUtilImpl()

    private val mainChecker = MainCheckerImpl()
    private val turnChanger = TurnChangerImpl()
    private val resigner = ResignerImpl()
    private val roller = RollerImpl()
    private val propertyBuyer = PropertyBuyerImpl()
    private val jailer = JailerImpl()
    private val houseBuilder: HouseBuilder = HouseBuilderImpl()
    private val pricer = PricerImpl()
    private val mortgager = MortgagerImpl()
    private val netWorthCalculator = NetWorthCalculatorImpl()

    private val SUGGESTED_MIN_CASH = 600

    override fun autoPlayGameUsingAi(game: Game) {
        log.info { "auto running a game.  Seed:${game.rule.seed}" }
        val errors = mainChecker.gameErrors(game)
        if (!errors.isEmpty()) {
            throw RuntimeException("Game has consistency errors: $errors")
        }
        while (true) {
            resignPlayersIfRollsExceeded(game)
            if (isTerminated(game)) {
                break
            }
            releasePlayerFromJail(game)
            roll(game)
            buyProperty(game)
            unmortgage(game)
            buyHouses(game)
            settleDebts(game)
            endTurnOrResignIfRollingDone(game)
        }
    }

    private fun resignPlayersIfRollsExceeded(game: Game) {
        if (game.turn.totalRolls < game.rule.maxTotalRolls) {
            return
        }
        val playerWithHighestNetWorth = playerWithHighestNetWorth(game)
        val players = game.players.filter { it != playerWithHighestNetWorth }.filter { !it.lost }.toSet()
        log.info { "Max total rolls exceeded (${game.rule.maxTotalRolls}).  The player with the highest net worth is player#${playerWithHighestNetWorth.id}. All others will resign." }
        players.forEach {
            resigner.resign(it, game)
        }
    }

    private fun playerWithHighestNetWorth(game: Game): Player {
        val players = gameUtil.playersStillInGame(game)
        if (players.isEmpty()) {
            throw RuntimeException("No players left in game. Can't calculate player with highest net worth")
        }
        var highestNetWorthPlayer = players.first()
        var highestNetWorth = Int.MIN_VALUE
        players.forEach {
            val netWorth = netWorthCalculator.netWorth(it, game)
            log.debug { "Player#${it.id} has a net worth of $${netWorth}" }
            if (netWorth > highestNetWorth) {
                highestNetWorth = netWorth
                highestNetWorthPlayer = it
            }
        }
        log.debug { "Player#${highestNetWorthPlayer.id} has the highest net worth: $${highestNetWorth}" }
        return highestNetWorthPlayer
    }


    private fun isTerminated(game: Game): Boolean {
        if (gameUtil.isGameOver(game)) {
            val player = gameUtil.winner(game)!!
            val totalAssets = netWorthCalculator.netWorth(player, game)
            log.info { "Game over. Player#${player.id} won.   WinnersTotalAssets:$${totalAssets}.  GameTotalRolls=${game.turn.totalRolls} Seed=${game.rule.seed}" }
            return true
        }
        return false
    }

    private fun endTurnOrResignIfRollingDone(game: Game) {
        when {
            roller.mayRoll(game) -> {
                log.debug { "player gets another roll" }
            }
            turnChanger.mayEndTurn(game) -> turnChanger.endTurn(game)
            else -> {
                resignCurrentPlayer(game)
                turnChanger.endTurn(game)
            }
        }
    }

    private fun resignCurrentPlayer(game: Game){
        resigner.resign(gameUtil.currentPlayer(game), game)
    }

    private fun roll(game: Game) {
        if (roller.mayRoll(game)) {
            roller.roll(game)
        }
    }

    private fun releasePlayerFromJail(game: Game) {
        val player = gameUtil.currentPlayer(game)
        if (!player.jailed) {
            return
        }
        log.debug { "Player#${player.id} is jailed.  Attempting to free..." }
        if (player.freeBailCards == 0) {
            mortgageAndSellHousesUntilPlayerHasMinimumCash(game.rule.bail, game)
        }
        when {
            jailer.mayFree(OutOfJailWay.CARD, game) -> jailer.free(OutOfJailWay.CARD, game)
            jailer.mayFree(OutOfJailWay.BAIL, game) -> jailer.free(OutOfJailWay.BAIL, game)
            else -> log.debug { "Couldn't find a way to get player#${player.id} out of jail" }
        }
    }


    private fun buyProperty(game: Game) {
        val player = gameUtil.currentPlayer(game)
        if (propertyBuyer.propertyForSale(game)) {
            val property = gameUtil.currentProperty(game)!!
            val price = pricer.propertyBuy(property, game)
            if (player.cash < price) {
                mortgageAndSellHousesUntilPlayerHasMinimumCash(price, game)
            }
        }
        if (propertyBuyer.mayBuyProperty(game)) {
            propertyBuyer.buyProperty(game)
        }
    }

    private fun settleDebts(game: Game) {
        mortgageAndSellHousesUntilPlayerHasMinimumCash(0, game)
    }

    private fun mortgageAndSellHousesUntilPlayerHasMinimumCash(minCash: Int, game: Game) {
        generateMoneyByMortgaging(minCash, game)
        generateMoneyBySellingHouses(minCash, game)
    }

    private fun generateMoneyByMortgaging(minCash: Int, game: Game) {
        val player = gameUtil.currentPlayer(game)
        val properties = gameUtil.currentPlayersProperties(game).filter{mortgager.mayMortgage(it, game)}.toMutableSet()
        while (!properties.isEmpty() && player.cash < minCash) {
            val property = properties.first()
            properties.remove(property)
            mortgager.mortgage(property, game)
        }
    }

    private fun generateMoneyBySellingHouses(minCash: Int, game: Game) {
        val player = gameUtil.currentPlayer(game)
        if (player.cash >= minCash) {
            return
        }
        while (player.cash < minCash) {
            val properties = gameUtil.currentPlayersProperties(game).filter { houseBuilder.isChangeHouseLegal(it, false, game) }.toMutableSet()
            if (properties.isEmpty()) {
                break
            }
            val property = properties.first()
            houseBuilder.changeHouse(property, false, game)
        }
    }

    private fun buyHouses(game: Game) {
        while (nextPropertyToBuyHouse(game) != null) {
            val property = nextPropertyToBuyHouse(game)!!
            houseBuilder.changeHouse(property, true, game)
        }
    }


    private fun unmortgage(game: Game) {
        while (nextPropertyToUnmortgage(game) != null) {
            val property = nextPropertyToUnmortgage(game)!!
            mortgager.unmortgage(property, game)
        }
    }

    private fun nextPropertyToUnmortgage(game: Game): Property? {
        val player = gameUtil.currentPlayer(game)
        val properties = gameUtil.currentPlayersProperties(game).filter { it.mortgaged }.toMutableSet()
        val property = properties.filter { mortgager.mayUnmortgage(it, game) }.firstOrNull()
        if (property == null) {
            return null
        }
        val price = pricer.unmortgage(property, game)
        if ((player.cash - price) < SUGGESTED_MIN_CASH) {
            return null
        }
        return property
    }


    private fun nextPropertyToBuyHouse(game: Game): Property? {
        val player = gameUtil.currentPlayer(game)
        val properties = gameUtil.currentPlayersProperties(game)
        val property = properties.filter { houseBuilder.isChangeHouseLegal(it, true, game) }.firstOrNull()
        if (property == null) {
            return null
        }
        val price = pricer.houseBuy(property, game)
        if ((player.cash - price) < SUGGESTED_MIN_CASH) {
            return null
        }
        return property
    }


}