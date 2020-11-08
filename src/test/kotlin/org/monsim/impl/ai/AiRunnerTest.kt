package org.monsim.impl.ai

import mu.KotlinLogging
import org.junit.jupiter.api.Test
import org.monsim.bean.domain.Game

class AiRunnerTest {
    private val log = KotlinLogging.logger {}
    private val runner = AiRunnerImpl()


    @Test
    fun run() {
        val game = Game()
        runner.autoPlayGameUsingAi(game)
        game.properties.forEach{
            log.info{"property: $it"}
        }
        game.players.forEach{
            log.info{"player: $it"}
        }
    }





}