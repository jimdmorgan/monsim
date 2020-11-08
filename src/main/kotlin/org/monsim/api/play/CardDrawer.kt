package org.monsim.api.play

import org.monsim.bean.domain.Card
import org.monsim.bean.domain.Game
import org.monsim.bean.domain.Group
import org.monsim.bean.domain.Pile
import org.monsim.bean.domain.Space

interface CardDrawer {

    fun drawCard(pile: Pile, game: Game): Card


}