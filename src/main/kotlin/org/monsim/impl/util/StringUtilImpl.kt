package org.monsim.impl.util

import org.monsim.bean.domain.Player

class StringUtilImpl {
    fun player(player: Player?) :String{
        return if (player == null) "Bank" else "player#${player.id}"
    }
}
