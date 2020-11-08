package org.monsim.bean.domain

import org.monsim.bean.Constants

data class Player  (
        val id: Int,
        var spaceId: Int = 1001, //the space the player is on
        var cash: Int = 1500, //how much cash the player has.  If negative, the player is in debt (to creditorId)
        var creditorId: Int? = Constants.BANK_ID, //If the player is in debt, this refers to the the playerId to whom the player owes the money
        var lost: Boolean = false, //true once the player has resigned
        var jailed: Boolean = false, //true if the player in jail
        var freeBailCards: Int = 0, //the number of 'Get out of jail free cards" the player has
        var rollsInJail: Int = 0 //the number of attempts the player has tried to roll doubles while in jail (players only have 3 until they must pay bail)

)

