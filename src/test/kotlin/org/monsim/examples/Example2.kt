package org.monsim.examples

import mu.KotlinLogging
import org.junit.jupiter.api.Test
import org.monsim.api.ai.AiRunner
import org.monsim.bean.domain.Game
import org.monsim.bean.domain.Player
import org.monsim.bean.domain.Rule
import org.monsim.impl.util.GameUtilImpl
import org.monsim.impl.ai.AiRunnerImpl

class Example2 {


    private val log = KotlinLogging.logger {}
    private val gameUtil = GameUtilImpl()

    @Test
    fun run() {
        val player1 = Player(id = 501, cash = 1500)
        val player2 = Player(id = 502, cash = 1500)
        val game = Game(
                rule = Rule(
                        maxTotalRolls = 5000,
                        isMortgagingDisabled = true,
                        isBuyingPropertiesDisabled = true,
                        seed = 12348
                ),
                cards = listOf(),
                taxes = listOf(),
                piles = listOf(),
                players = listOf(player1, player2)
        )
        gameUtil.propertiesWithGroupName("DarkBlue", game).forEach { it.ownerId = player1.id }
        gameUtil.propertiesWithGroupName("Pink", game).forEach { it.ownerId = player2.id }

        val aiRunner: AiRunner = AiRunnerImpl()
        aiRunner.autoPlayGameUsingAi(game)

    }


}

