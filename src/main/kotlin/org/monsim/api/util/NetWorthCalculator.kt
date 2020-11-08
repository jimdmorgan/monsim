package org.monsim.api.util

import org.monsim.bean.domain.Card
import org.monsim.bean.domain.Game
import org.monsim.bean.domain.Group
import org.monsim.bean.domain.Pile
import org.monsim.bean.domain.Player
import org.monsim.bean.domain.Space

interface NetWorthCalculator {

    fun netWorth(player: Player, game: Game): Int


}