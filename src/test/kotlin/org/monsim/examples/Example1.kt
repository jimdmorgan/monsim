package org.monsim.examples

import mu.KotlinLogging
import org.junit.jupiter.api.Test
import org.monsim.api.ai.AiRunner
import org.monsim.bean.domain.Game
import org.monsim.bean.domain.Player
import org.monsim.bean.domain.Rule
import org.monsim.impl.util.GameUtilImpl
import org.monsim.impl.ai.AiRunnerImpl

/** See README.md for documentation */
class Example1 {


    private val log = KotlinLogging.logger {}
    private val gameUtil = GameUtilImpl()

    @Test
    fun run() {
        val player1 = Player(id = 501, cash = 100)
        val player2 = Player(id = 502, cash = 100)
        val game = Game(
                rule = Rule(
                        maxTotalRolls = 5000,
                        isBuyingHousesDisabled = true,
                        isMortgagingDisabled = true,
                        isBuyingPropertiesDisabled = true,
                        cashForPassingGo = 0,
                        bail = 0,
                        seed = 12345
                ),
                cards = listOf(),
                taxes = listOf(),
                piles = listOf(),
                players = listOf(player1, player2)
        )
        gameUtil.propertyWithName("Water Works", game).ownerId = player1.id
        gameUtil.propertyWithName("B&O RR", game).ownerId = player2.id
        val aiRunner:AiRunner = AiRunnerImpl()
        aiRunner.autoPlayGameUsingAi(game)
    }


}

