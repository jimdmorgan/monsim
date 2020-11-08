package org.monsim.api.play

import org.monsim.bean.DiceRoll
import org.monsim.bean.RollLogic
import org.monsim.bean.domain.Card
import org.monsim.bean.domain.Game
import org.monsim.bean.domain.Group
import org.monsim.bean.domain.Pile
import org.monsim.bean.domain.Player
import org.monsim.bean.domain.Space

interface Resigner {

    fun resign(player: Player, game: Game)


}