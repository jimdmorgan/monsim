package org.monsim.bean.domain

data class Rule(
        val maxTotalRolls: Long = 9, //maximum number of rolls before game is declared tie between remaining players
        val seed: Long = 12345L, //a seed that determines the random rolls of the dice and order of chance cards
        val maxHouses: Int = 5, //5 houses is the max a player can build on a monopoly (5th "house" is a hotel)
        val maxDoubles: Int = 2, //users can roll 2 doubles.  on the third double, they go to jail
        val bail: Int = 50, //user must pay $50 to get out of jail
        val maxRollsInJail: Int = 3, //when in jail, users can attempt to roll 3 times before they must pay bail
        val cashForPassingGo: Int = 200, //players are paid $200 when they pass go
        val mortgagePercent: Int = 50,   //players gets 50% of buyPrice for mortgaging an owned property
        val unmortgagePercent: Int = 55, //players must pay 55% of buyPrice to unmortgage an owned property
        val monopolyWithoutHousesRentPercent: Int = 200, //players receive double rent (200%) if they have a monopoly with no houses.
        val houseSellBackPercent: Int = 50, //players sell houses back to bank at 50% of the price they paid

        val goSpaceId: Int = 1001,   //the spaceId for GO
        val jailSpaceId: Int = 1011, //the spaceId for JAIL
        val gotoJailSpaceIds: Set<Int> = setOf<Int>(1031), //the set of spaceIds that send the user to jail
        val freeSpaceIds: Set<Int> = setOf<Int>(1021), //the spaceIds for free parking

        val isBuyingPropertiesDisabled: Boolean = false, //true if users aren't allowed to buy properties
        val isBuyingHousesDisabled: Boolean = false, //true if users aren't allowed to buy houses
        val isMortgagingDisabled: Boolean = false, //true if users aren't allowed to mortgage and unmortgage
)
