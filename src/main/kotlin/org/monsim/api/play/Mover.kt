package org.monsim.api.play

import org.monsim.bean.DiceRoll
import org.monsim.bean.domain.Game


interface Mover {

    fun move(spacesToMove:Int, game: Game): Unit


}