package org.monsim.api.play

import org.monsim.bean.DiceRoll
import org.monsim.bean.RollLogic
import org.monsim.bean.domain.Card
import org.monsim.bean.domain.Game
import org.monsim.bean.domain.Group
import org.monsim.bean.domain.Pile
import org.monsim.bean.domain.Space

interface Roller {

    fun roll(game: Game)
    fun mayRoll(game: Game): Boolean

    fun rollLogic(diceRoll: DiceRoll, game: Game): RollLogic
    fun applyRollLogic(rollLogic: RollLogic, game: Game): Unit

}