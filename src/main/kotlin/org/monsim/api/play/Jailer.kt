package org.monsim.api.play

import org.monsim.bean.domain.Game
import org.monsim.bean.domain.Player
import org.monsim.bean.type.OutOfJailWay

interface Jailer {

    fun jail(game: Game)
    fun free(outOfJailWay: OutOfJailWay, game: Game)
    fun mayFree(outOfJailWay: OutOfJailWay, game: Game): Boolean
}