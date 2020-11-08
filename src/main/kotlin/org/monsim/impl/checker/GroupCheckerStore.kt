package org.monsim.impl.checker

import org.monsim.bean.domain.Game
import org.monsim.bean.domain.Group
import org.monsim.bean.type.GroupType
import org.monsim.impl.util.UniqueUtilImpl
import java.lang.RuntimeException

class GroupCheckerStore {

    abstract class Group_Abstract_Checker(private val game: Game) : AbstractCheckerStore.Abstract_SingleItem_Checker(game) {
        val uniqueUtil = UniqueUtilImpl()
        val groupIds = game.groups.map{it.id}.toSet()

        final override fun itemsToCheck(): List<Any> {
            return game.groups
        }

        final override fun itemError(item: Any): String? {
            val group = item as Group
            var error = groupError(group)
            if (error != null){
                error = "Bad Group#${group.id}: $error"
            }
            return error
        }

        abstract fun groupError(group: Group): String?
    }

    class Group_Rent_Checker(val game: Game) : Group_Abstract_Checker(game) {

        private val rentCountMap = game.groups.map{it.id to expectedRentCount(it)}.toMap()


        override fun groupError(group: Group): String? {
            val groupId = group.id
            val expectedRentCount = rentCountMap.get(groupId) //6 in the case of DarkBlue (50,200,600,1400,1700,2000)
            val rents = group.rents
            if (rents.size != group.buyPrices.size){
                return "group buyPrices has ${group.buyPrices.size} schedule(s), but rents have ${rents.size}"
            }
            rents.forEachIndexed { index, scheduleRents -> //scheduleRents for DarkBlue would be 50,200,600,1400,1700,2000 (for schedule#2)
                val schedule = index + 1   //2 in our example
                val rentCount = scheduleRents.size //6 is expected
                if (rentCount != expectedRentCount) {
                    return "rents for schedule#${schedule} should have ${expectedRentCount} values but has ${rentCount}"
                }
                var lastRent = Integer.MIN_VALUE
                scheduleRents.forEach{
                    val rent = it
                    if (rent < 0) {
                        return "rent for schedule#${schedule} has negative rent '${rent}'"
                    }
                    if (rent < lastRent) {
                        return("rent for schedule#${schedule} lessens since '${rent}' is less than previous rent '${lastRent}'")
                    }
                    lastRent = rent
                }
            }
            return null
        }

        private fun expectedRentCount(group: Group): Int {
            var groupType = group.groupType
            val rentCount = when (groupType) {
                GroupType.DEVELOPABLE -> game.rule.maxHouses + 1 //first listing is rent if there are 0 houses
                GroupType.DICE, GroupType.FIXED -> numberOfPropertiesInGroup(group)
                else -> throw RuntimeException("Bad groupType: ${groupType}")
            }
            return rentCount
        }

        private fun numberOfPropertiesInGroup(group: Group): Int {
            val count = game.properties.filter { it.groupId == group.id }.count()
            return count
        }


    }


    class Group_HousePrice_Checker(val game: Game) : Group_Abstract_Checker(game) {
        override fun groupError(group: Group): String? {
            val groupId = group.id
            val housePrice = group.housePrice
            val isDevelopable = group.groupType == GroupType.DEVELOPABLE
            val isHousePriced = housePrice != null
            var error = when {
                !isDevelopable && !isHousePriced -> null
                isDevelopable && !isHousePriced -> "should have a house price"
                !isDevelopable && isHousePriced -> "shouldn't have a house price"
                isDevelopable && isHousePriced -> errorIfInvalidHousePrice(housePrice!!)
                else -> throw RuntimeException("when logic error housePrice")
            }
            return error
        }

        private fun errorIfInvalidHousePrice(housePrice: Int): String? {
            return if (housePrice >= 0) null else "negative housePrice '$housePrice'"
        }
    }

    class Group_BuyPrice_Checker(val game: Game) : Group_Abstract_Checker(game) {
        override fun groupError(group: Group): String? {
            val buyPrices = group.buyPrices
            if (buyPrices.isEmpty()){
                return "buyPrices is empty"
            }
            val negativeBuyPrices = buyPrices.filter{it < 0}.toSet()
            if (!negativeBuyPrices.isEmpty()){
                return "there are negative buyPrices: ${negativeBuyPrices}"
            }
            return null
        }

    }


    class Group_IdsUnique_Checker(val game: Game) : Group_Abstract_Checker(game) {
        val repeatedGroupIds = uniqueUtil.repeatedInts(
                game.groups.map{it.id}.toList())
        override fun groupError(group: Group): String? {
            val groupId = group.id
            val error = if (!repeatedGroupIds.contains(groupId)) null else
                "duplicate id '$groupId'"
            return error
        }
    }

    class Group_NameUnique_Checker(val game: Game) : Group_Abstract_Checker(game) {
        val repeatedGroupNames = uniqueUtil.repeatedStrings(
                game.groups.map{it.name}.toList())
        override fun groupError(group: Group): String? {
            val groupName = group.name
            val error = if (!repeatedGroupNames.contains(groupName)) null else
                "duplicate name '$groupName'"
            return error
        }
    }






}