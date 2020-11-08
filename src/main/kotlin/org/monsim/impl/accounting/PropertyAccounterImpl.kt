package org.monsim.impl.accounting

import mu.KotlinLogging
import org.monsim.api.accounting.PropertyAccounter
import org.monsim.bean.Constants
import org.monsim.bean.domain.Game
import org.monsim.bean.domain.Player
import org.monsim.bean.domain.Property
import org.monsim.impl.util.GameUtilImpl
import org.monsim.impl.util.MonopolyUtilImpl
import org.monsim.impl.util.StringUtilImpl
import java.lang.RuntimeException

class PropertyAccounterImpl : PropertyAccounter {

    private val monopolyUtil = MonopolyUtilImpl()
    private val gameUtil = GameUtilImpl()
    private val stringUtil = StringUtilImpl()
    private val log = KotlinLogging.logger {}


    override fun transferProperty(property: Property, to: Player?, game: Game) {
        val oldOwner = gameUtil.playerForId(property.ownerId, game)
        log.debug{"PropertyTransfer From:${stringUtil.player(oldOwner)} To:${stringUtil.player(to)} PropertyId:#${property.id} (mortgaged:${property.mortgaged})"}
        val group = gameUtil.groupForProperty(property, game)
        when{
            monopolyUtil.isGroupWithHouses(group, game) -> throw RuntimeException("Can't transfer property because group still has houses: ${property}")
            to != null && to.lost -> throw RuntimeException("Can't transfer property to player that has lost")
        }
        val newOwnerId =  if (to == Constants.BANK) Constants.BANK_ID else to!!.id
        property.ownerId = newOwnerId
    }


}