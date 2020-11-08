package org.monsim.impl.play

import mu.KotlinLogging
import org.monsim.api.play.FreeParker
import org.monsim.bean.domain.Game
import org.monsim.impl.util.GameUtilImpl

class FreeParkerImpl : FreeParker {

    private val log = KotlinLogging.logger {}
    private val gameUtil = GameUtilImpl()

    override fun park(game: Game) { //in some variants player receives money for landing on free parking
        val player = gameUtil.currentPlayer(game)
        log.debug{"free parking for player#${player.id}: no action"}
    }



}