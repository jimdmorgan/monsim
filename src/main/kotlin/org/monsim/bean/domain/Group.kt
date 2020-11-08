package org.monsim.bean.domain


import org.monsim.bean.type.GroupType

/** Groups represent a group of properties -- such as the darkBlue group which comprises Park Place & Boardwalk */
data class Group(
        val id: Int,
        val name: String = "", //example: railroad, brown, lightblue, pink, orange, red, yellow, green, darkblue
        val groupType: GroupType = GroupType.FIXED, //see GroupType for details
        val buyPrices: List<Int> = listOf(), //list of buy prices for the group. e.g. for DARK_BLUE: listOf(350, 400); the size of this list determines number of schedules (referenced by rents and property.schedule)
        val rents: List<List<Int>> = listOf(), //see below note on rents
        val housePrice: Int? = null //the price to build a house (null if GroupType isn't DEVELOPABLE)
)

/*
Schedules: Groups can have multiple "schedules", meaning that two different properties in that group might have a different buyPrice and a different rent

==Rents==
"Rents" represent 1 of 3 things, depending on groupType.
1) DEVELOPABLE GROUPTYPES: rent depends on number of houses, (example: DARK_BLUE)
Example: listOf( listOf(35, 175, 500, 1100, 1300, 1500), listOf(50, 200, 600, 1400, 1700, 2000))
In this case, 600 means rent is $600 if 2 houses are owned, for schedule 2 on the dark blue group, which is property Boardwalk.
2) FIXED: rent depends on number of properties owned, (example: Railsroads group)
Example: listOf(listOf(25, 50, 100, 200))  In this case, 100 means rent is $100 if 3 properties are owned)
3) DICE: rent depends on dice multiplier, (example: Utilities group)
Example: listOf(listOf(4, 10))  (10 means rent is [10 multiplied by diceRolled] if 2 properties are owned)
*/