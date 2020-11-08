package org.monsim.api.accounting

import org.monsim.bean.domain.Game
import org.monsim.bean.domain.Player
import org.monsim.bean.domain.Property

interface PropertyAccounter {

    fun transferProperty(property: Property, to: Player?, game: Game)
}