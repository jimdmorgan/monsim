package org.monsim.api.play

import org.monsim.bean.domain.Game
import org.monsim.bean.domain.Player
import org.monsim.bean.domain.Tax

interface MultiPlayerPayer {

    fun cashToOtherPlayers(amount: Int, game: Game)
    fun cashFromOtherPlayers(amount: Int, game: Game)

}