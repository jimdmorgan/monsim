package org.monsim.impl.util

import org.monsim.api.util.MonopolyUtil
import org.monsim.bean.domain.Game
import org.monsim.bean.domain.Group
import org.monsim.bean.type.GroupType

class MonopolyUtilImpl : MonopolyUtil {

    val gameUtil = GameUtilImpl()



    override fun isGroupMonopoly(group: Group, game: Game): Boolean {
        val owners = gameUtil.propertiesInGroup(group, game).map{it.ownerId}.toSet()
        val isGroupMonopoly = owners.size == 1 && !owners.contains(null)
        return isGroupMonopoly
    }

    override fun isGroupWithHouses(group: Group, game: Game): Boolean {
        if (group.groupType != GroupType.DEVELOPABLE){
            return false
        }
        val houses = gameUtil.propertiesInGroup(group, game).map{it.houses}.filter{it != null && it > 0}.toSet()
        val isGroupWithHouses = houses.size > 0
        return isGroupWithHouses
    }



}