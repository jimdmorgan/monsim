package org.monsim.impl.checker

import org.monsim.bean.domain.Game
import org.monsim.bean.domain.Group
import org.monsim.bean.domain.Property
import org.monsim.bean.type.GroupType
import org.monsim.impl.util.UniqueUtilImpl
import java.lang.RuntimeException

class PropertyCheckerStore {

    abstract class Property_Abstract_Checker(private val game: Game) : AbstractCheckerStore.Abstract_SingleItem_Checker(game) {

        val uniqueUtil = UniqueUtilImpl()
        val groupMap = game.groups.map{it.id to it}.toMap()

        final override fun itemsToCheck(): List<Any> {
            return game.properties
        }

        final override fun itemError(item: Any): String? {
            val property = item as Property
            var error = propertyError(property)
            if (error != null){
                error = "Bad Property#${property.id}: $error"
            }
            return error
        }

        abstract fun propertyError(property: Property): String?

    }

    class Property_Houses_Checker(val game: Game) : Property_Abstract_Checker(game) {

        val groupLowHousesMap = game.groups.map{it.id to lowHouse(it)}.toMap()

        private fun lowHouse(group: Group): Int {
            val houseCountsForGroup = game.properties.filter{it.groupId == group.id}.filter{it.houses != null}.map{it.houses}.map{it!!}.toSet()
            val lowHouseCount = if (houseCountsForGroup.isEmpty()) 0 else houseCountsForGroup.min()!!
            return lowHouseCount
        }

        override fun propertyError(property: Property): String? {
            val groupId = property.groupId
            if (!groupMap.containsKey(groupId)){
                return null
            }
            val group = groupMap.get(groupId)!!
            val isDevelopable = group.groupType == GroupType.DEVELOPABLE
            val houses = property.houses
            val hasHouseValue = houses != null
            val error = when {
                isDevelopable && !hasHouseValue -> "shouldn't have null houses because group is developable"
                !isDevelopable && hasHouseValue -> "should have null houses because group isn't developable"
                !isDevelopable && !hasHouseValue -> null
                isDevelopable && hasHouseValue -> errorIfHousesNotValid(property)
                else -> throw RuntimeException("when logic error propertyHouseChecker")
            }
            return error
        }

        private fun errorIfHousesNotValid(property: Property): String? {
            val houses = property.houses!!
            val minHouses = 0
            val maxHouses = game.rule.maxHouses
            val rangeError = if (houses >= minHouses && houses <= maxHouses) null else
                "houses '$houses' isn't between $minHouses and $maxHouses"
            val mortgageError = if (!(property.mortgaged && houses > 0)) null else
                "houses '$houses' is positive yet property is mortgaged"
            val lowestHousesInGroup = groupLowHousesMap.get(property.groupId)!!
            val housesDifference = houses - lowestHousesInGroup
            val inconsistentError = if (housesDifference <= 1) null else
                "property has '$houses' houses yet another property in the same group only has '$lowestHousesInGroup' houses"
            val errors = setOf(rangeError, mortgageError, inconsistentError).filter{it != null}.toList()
            val error = if (errors.isEmpty()) null else errors[0]
            return error
        }
    }

    class Property_Schedule_Checker(val game: Game) : Property_Abstract_Checker(game) {

        val minSchedule = 1

        override fun propertyError(property: Property): String? {
            val groupId = property.groupId
            if (!groupMap.containsKey(groupId)){
                return null
            }
            val group = groupMap.get(groupId)!!
            val numberOfSchedules = group.buyPrices.size
            val maxSchedule = numberOfSchedules + 1
            val schedule = property.schedule
            val error = if (schedule >= minSchedule && schedule <= maxSchedule) null else
                "schedule '$schedule' isn't between '$minSchedule' and '$maxSchedule'"
            return error
        }
    }

    class Property_SpaceId_Checker(val game: Game) : Property_Abstract_Checker(game) {
        val validSpaceIds = game.spaces.map{it.id}.toSet()
        override fun propertyError(property: Property): String? {
            val spaceId = property.spaceId
            val error = if (validSpaceIds.contains(spaceId)) null else
                "invalid spaceId '$spaceId'"
            return error
        }
    }

    class Property_GroupId_Checker(val game: Game) : Property_Abstract_Checker(game) {
        override fun propertyError(property: Property): String? {
            val groupId = property.groupId
            val error = if (groupMap.contains(groupId)) null else
                "invalid groupId '$groupId'"
            return error
        }
    }

    class Property_OwnerId_Checker(val game: Game) : Property_Abstract_Checker(game) {
        val playerIds = game.players.filter{!it.lost}.map{it.id}.toSet()
        override fun propertyError(property: Property): String? {
            val ownerId = property.ownerId
            if (ownerId == null){
                return null;
            }
            val error = if (playerIds.contains(ownerId)) null else
                "ownerId '$ownerId' doesn't exist or lost"
            return error
        }
    }

    class Property_IdsUnique_Checker(val game: Game) : Property_Abstract_Checker(game) {
        val repeatedPropertyIds = uniqueUtil.repeatedInts(
                game.properties.map{it.id}.toList())
        override fun propertyError(property: Property): String? {
            val propertyId = property.id
            val error = if (!repeatedPropertyIds.contains(propertyId)) null else
                "duplicate id '$propertyId'"
            return error
        }
    }







}