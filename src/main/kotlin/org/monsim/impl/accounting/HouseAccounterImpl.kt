package org.monsim.impl.accounting

import mu.KotlinLogging
import org.monsim.api.accounting.HouseAccounter
import org.monsim.bean.domain.Game
import org.monsim.bean.domain.Group
import org.monsim.bean.domain.Property
import org.monsim.bean.Range
import org.monsim.bean.type.GroupType
import org.monsim.impl.util.GameUtilImpl
import org.monsim.impl.util.MonopolyUtilImpl
import java.lang.RuntimeException

class HouseAccounterImpl : HouseAccounter {

    private val gameUtil = GameUtilImpl()
    private val monopolyUtil = MonopolyUtilImpl()
    private val log = KotlinLogging.logger {}

    override fun transferHouse(property: Property, isAdded: Boolean, game: Game) {
        val error = transferHouseError(property, isAdded, game)
        val verb = if (isAdded) "add" else "remove"
        if (error != null){
            throw RuntimeException("Can't ${verb} house for property#${property.id}: $error")
        }
        alterHouses(property,isAdded)
    }


    private fun alterHouses(property: Property, isAddition: Boolean){
        val verb = if (isAddition) "gained" else "lost"
        val addition = if (isAddition) 1 else -1
        val oldAmount = property.houses!!
        val newAmount = oldAmount + addition
        property.houses = newAmount
        log.debug("Property#${property.id} ${verb} a house old:$oldAmount new:${newAmount}")
    }

    override fun transferHouseError(property: Property, isAdded: Boolean, game: Game): String? {
        val error = if (isAdded) errorIfHouseAddedToProperty(property, game) else errorIfHouseRemovedFromProperty(property, game)
        return error
    }

    fun errorIfHouseAddedToProperty(property: Property, game: Game): String? {
        if (game.rule.isBuyingHousesDisabled){
            return "game rule doesn't allow houses to be bought"
        }
        val group = gameUtil.groupForProperty(property, game)
        if (!isGroupAbleToChangeHouses(group, game)){
            return "group isn't developable"
        }
        if (!monopolyUtil.isGroupMonopoly(group, game)){
            return "no single owner of all properties"
        }
        if (!mortgagedPropertiesInGroup(group, game).isEmpty()){
            return "mortgaged properties in group"
        }
        val houses = property.houses!!
        if (houses >= game.rule.maxHouses){
            return "houses would exceed rule.maxHouses"
        }
        val range = houseRange(group, game)
        val propertyExistsWithLowerHouseCount = houses > range.min
        if (propertyExistsWithLowerHouseCount){
            return "property exists in group with lower house count"
        }
        return null
    }

    fun errorIfHouseRemovedFromProperty(property: Property, game: Game): String? {
        if (game.rule.isBuyingHousesDisabled){
            return "game rule doesn't allow houses to be bought (or sold)"
        }
        val group = gameUtil.groupForProperty(property, game)
        if (!isGroupAbleToChangeHouses(group, game)){
            return "group isn't developable"
        }
        val houses = property.houses!!
        if (houses <= 0){
            return "there are no houses on property"
        }
        val range = houseRange(group, game)
        val propertyExistsWithHigherHouseCount = houses < range.max
        if (propertyExistsWithHigherHouseCount){
            return "property exists in group with higher house count"
        }
        return null
    }



    private fun isGroupAbleToChangeHouses(group: Group, game: Game): Boolean{
        if (group.groupType != GroupType.DEVELOPABLE){
            return false
        }
        val houseCounts = houseCountsForGroup(group, game)
        if (houseCounts.isEmpty()){
            return false
        }
        return true
    }

    private fun mortgagedPropertiesInGroup(group: Group, game: Game): Set<Property>{
        val properties = gameUtil.propertiesInGroup(group, game)
        return properties.filter{it.mortgaged}.toSet()
    }




    private fun houseRange(group: Group, game: Game): Range {
        val houseCounts = houseCountsForGroup(group, game)
        val minHouseCount = houseCounts.minOrNull()
        val maxHouseCount = houseCounts.maxOrNull()
        val range = Range(min = minHouseCount!!, max = maxHouseCount!!)
        return range
    }

    private fun houseCountsForGroup(group: Group, game: Game): Set<Int>{
        val properties = gameUtil.propertiesInGroup(group, game)
        val houseCounts = properties.map{it.houses}.filter{it != null}.map{it!!}.toSet()
        return houseCounts
    }

}