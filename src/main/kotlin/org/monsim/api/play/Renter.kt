package org.monsim.api.play

import org.monsim.bean.domain.Game
import org.monsim.bean.domain.Player
import org.monsim.bean.domain.Property

interface Renter {

    fun chargeRentIfNecessary(game: Game)

}