package org.monsim.api.play

import org.monsim.bean.DiceRoll
import org.monsim.bean.RollLogic
import org.monsim.bean.domain.Card
import org.monsim.bean.domain.Game
import org.monsim.bean.domain.Group
import org.monsim.bean.domain.Pile
import org.monsim.bean.domain.Space

interface TurnChanger {

    fun endTurn(game: Game)
    fun mayEndTurn(game: Game): Boolean


}