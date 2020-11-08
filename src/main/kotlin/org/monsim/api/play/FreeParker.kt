package org.monsim.api.play

import org.monsim.bean.domain.Game
import org.monsim.bean.domain.Player
import org.monsim.bean.domain.Tax

interface FreeParker {

    fun park(game: Game)

}