package org.monsim.bean

import org.monsim.bean.domain.Card
import org.monsim.bean.domain.Group
import org.monsim.bean.domain.Pile
import org.monsim.bean.domain.Player
import org.monsim.bean.domain.Property
import org.monsim.bean.domain.Space
import org.monsim.bean.domain.Tax
import org.monsim.bean.type.CardType
import org.monsim.bean.type.GroupType

data class DefaultGameValues(

        val players: List<Player> = listOf<Player>(
                Player(
                        id = 501,
                        cash = 1500,
                        jailed = false,
                        freeBailCards = 0,
                        lost = false,
                        rollsInJail = 0,
                        spaceId = 1001
                ),
                Player(
                        id = 502,
                        cash = 1500,
                        jailed = false,
                        freeBailCards = 0,
                        lost = false,
                        rollsInJail = 0,
                        spaceId = 1001
                ),
                Player(
                        id = 503,
                        cash = 1500,
                        jailed = false,
                        freeBailCards = 0,
                        lost = false,
                        rollsInJail = 0,
                        spaceId = 1001
                ),
                Player(
                        id = 504,
                        cash = 1500,
                        jailed = false,
                        freeBailCards = 0,
                        lost = false,
                        rollsInJail = 0,
                        spaceId = 1001
                )
        ),
        val taxes: List<Tax> = listOf(
                Tax(amount = 200, spaceId = 1005),
                Tax(amount = 100, spaceId = 1039)
        ),
        val groups: List<Group> = listOf<Group>(
                Group(
                        id = 8001,
                        name = "Brown",
                        groupType = GroupType.DEVELOPABLE,
                        housePrice = 50,
                        buyPrices = listOf(60, 60),
                        rents = listOf(
                                listOf(2, 10, 30, 90, 160, 250),
                                listOf(4, 20, 60, 180, 320, 450)
                        )
                ),
                Group(
                        id = 8002,
                        name = "LightBlue",
                        groupType = GroupType.DEVELOPABLE,
                        housePrice = 50,
                        buyPrices = listOf(100, 120),
                        rents = listOf(
                                listOf(6, 30, 90, 270, 400, 550),
                                listOf(8, 40, 100, 300, 450, 600)
                        )
                ),
                Group(
                        id = 8003,
                        name = "Pink",
                        groupType = GroupType.DEVELOPABLE,
                        housePrice = 100,
                        buyPrices = listOf(140, 160),
                        rents = listOf(
                                listOf(10, 50, 150, 450, 625, 750),
                                listOf(12, 60, 180, 500, 700, 900)
                        )
                ),
                Group(
                        id = 8004,
                        name = "Orange",
                        groupType = GroupType.DEVELOPABLE,
                        housePrice = 100,
                        buyPrices = listOf(180, 200),
                        rents = listOf(
                                listOf(14, 70, 200, 550, 750, 950),
                                listOf(16, 80, 220, 600, 800, 1000)
                        )
                ),
                Group(
                        id = 8005,
                        name = "Red",
                        groupType = GroupType.DEVELOPABLE,
                        housePrice = 150,
                        buyPrices = listOf(220, 240),
                        rents = listOf(
                                listOf(18, 90, 250, 700, 875, 1050),
                                listOf(20, 100, 300, 750, 925, 1100)
                        )
                ),
                Group(
                        id = 8006,
                        name = "Yellow",
                        groupType = GroupType.DEVELOPABLE,
                        housePrice = 150,
                        buyPrices = listOf(260, 280),
                        rents = listOf(
                                listOf(22, 110, 330, 800, 975, 1150),
                                listOf(24, 120, 360, 850, 1025, 1200)
                        )
                ),
                Group(
                        id = 8007,
                        name = "Green",
                        groupType = GroupType.DEVELOPABLE,
                        housePrice = 200,
                        buyPrices = listOf(300, 320),
                        rents = listOf(
                                listOf(26, 130, 390, 900, 1100, 1275),
                                listOf(28, 150, 450, 1000, 1200, 1400)
                        )
                ),
                Group(
                        id = 8008,
                        name = "DarkBlue",
                        groupType = GroupType.DEVELOPABLE,
                        housePrice = 200,
                        buyPrices = listOf(350, 400),
                        rents = listOf(
                                listOf(35, 175, 500, 1100, 1300, 1500),
                                listOf(50, 200, 600, 1400, 1700, 2000)
                        )
                ),
                Group(
                        id = 8009,
                        name = "Railroads",
                        groupType = GroupType.FIXED,
                        buyPrices = listOf(200),
                        rents = listOf(listOf(25, 50, 100, 200))
                ),
                Group(
                        id = 8010,
                        name = "Utilities",
                        groupType = GroupType.DICE,
                        buyPrices = listOf(150),
                        rents = listOf(listOf(4, 10))
                )
        ),
        val spaces: List<Space> = listOf<Space>(
                Space(
                        id = 1001,
                        name = "GO"
                ),
                Space(
                        id = 1002,
                        name = "Mediterranean Ave"
                ),
                Space(
                        id = 1003,
                        name = "Community Chest 1"
                ),
                Space(
                        id = 1004,
                        name = "Baltic Ave"
                ),
                Space(
                        id = 1005,
                        name = "Income Tax"
                ),
                Space(
                        id = 1006,
                        name = "Reading RR"
                ),
                Space(
                        id = 1007,
                        name = "Oriental Ave"
                ),
                Space(
                        id = 1008,
                        name = "Chance 1"
                ),
                Space(
                        id = 1009,
                        name = "Vermont Ave"
                ),
                Space(
                        id = 1010,
                        name = "Connecticut Ave"
                ),
                Space(
                        id = 1011,
                        name = "Jail"
                ),
                Space(
                        id = 1012,
                        name = "St. Charles Pl"
                ),
                Space(
                        id = 1013,
                        name = "Electric Co"
                ),
                Space(
                        id = 1014,
                        name = "States Ave"
                ),
                Space(
                        id = 1015,
                        name = "Virginia Ave"
                ),
                Space(
                        id = 1016,
                        name = "Pennsylvania RR"
                ),
                Space(
                        id = 1017,
                        name = "St. James Pl"
                ),
                Space(
                        id = 1018,
                        name = "Community Chest 2"
                ),
                Space(
                        id = 1019,
                        name = "Tennessee Ave"
                ),
                Space(
                        id = 1020,
                        name = "New York Ave"
                ),
                Space(
                        id = 1021,
                        name = "Free Parking"
                ),
                Space(
                        id = 1022,
                        name = "Kentucky Ave"
                ),
                Space(
                        id = 1023,
                        name = "Chance 2"
                ),
                Space(
                        id = 1024,
                        name = "Indiana Ave"
                ),
                Space(
                        id = 1025,
                        name = "Illinois Ave"
                ),
                Space(
                        id = 1026,
                        name = "B&O RR"
                ),
                Space(
                        id = 1027,
                        name = "Atlantic Ave"
                ),
                Space(
                        id = 1028,
                        name = "Ventnor Ave"
                ),
                Space(
                        id = 1029,
                        name = "Water Works"
                ),
                Space(
                        id = 1030,
                        name = "Marvin Gardens"
                ),
                Space(
                        id = 1031,
                        name = "Go to Jail"
                ),
                Space(
                        id = 1032,
                        name = "Pacific Ave"
                ),
                Space(
                        id = 1033,
                        name = "N. Carolina Ave"
                ),
                Space(
                        id = 1034,
                        name = "Community Chest 3"
                ),
                Space(
                        id = 1035,
                        name = "Pennsylvania Ave"
                ),
                Space(
                        id = 1036,
                        name = "Short Line RR"
                ),
                Space(
                        id = 1037,
                        name = "Chance 3"
                ),
                Space(
                        id = 1038,
                        name = "Park Pl"
                ),
                Space(
                        id = 1039,
                        name = "Luxury Tax"
                ),
                Space(
                        id = 1040,
                        name = "Boardwalk"
                )
        ),
        val properties: List<Property> = listOf<Property>(
                Property(
                        id = 2001,
                        groupId = 8001,
                        spaceId = 1002,
                ),
                Property(
                        id = 2002,
                        groupId = 8001,
                        spaceId = 1004,
                        schedule = 2
                ),
                Property(
                        id = 2003,
                        groupId = 8002,
                        spaceId = 1007
                ),
                Property(
                        id = 2004,
                        groupId = 8002,
                        spaceId = 1009
                ),
                Property(
                        id = 2005,
                        groupId = 8002,
                        spaceId = 1010,
                        schedule = 2
                ),
                Property(
                        id = 2006,
                        groupId = 8003,
                        spaceId = 1012
                ),
                Property(
                        id = 2007,
                        groupId = 8003,
                        spaceId = 1014
                ),
                Property(
                        id = 2008,
                        groupId = 8003,
                        spaceId = 1015,
                        schedule = 2
                ),
                Property(
                        id = 2009,
                        groupId = 8004,
                        spaceId = 1017
                ),
                Property(
                        id = 2010,
                        groupId = 8004,
                        spaceId = 1019
                ),
                Property(
                        id = 2011,
                        groupId = 8004,
                        spaceId = 1020,
                        schedule = 2
                ),
                Property(
                        id = 2012,
                        groupId = 8005,
                        spaceId = 1022
                ),
                Property(
                        id = 2013,
                        groupId = 8005,
                        spaceId = 1024
                ),
                Property(
                        id = 2014,
                        groupId = 8005,
                        spaceId = 1025,
                        schedule = 2
                ),
                Property(
                        id = 2015,
                        groupId = 8006,
                        spaceId = 1027
                ),
                Property(
                        id = 2016,
                        groupId = 8006,
                        spaceId = 1028
                ),
                Property(
                        id = 2017,
                        groupId = 8006,
                        spaceId = 1030,
                        schedule = 2
                ),
                Property(
                        id = 2018,
                        groupId = 8007,
                        spaceId = 1032,
                ),
                Property(
                        id = 2019,
                        groupId = 8007,
                        spaceId = 1033,
                ),
                Property(
                        id = 2020,
                        groupId = 8007,
                        spaceId = 1035,
                        schedule = 2,
                ),
                Property(
                        id = 2021,
                        groupId = 8008,
                        spaceId = 1038,
                ),
                Property(
                        id = 2022,
                        groupId = 8008,
                        spaceId = 1040,
                        schedule = 2,
                ),
                Property(
                        id = 2023,
                        groupId = 8009,
                        spaceId = 1006,
                        houses = null
                ),
                Property(
                        id = 2024,
                        groupId = 8009,
                        spaceId = 1016,
                        houses = null
                ),
                Property(
                        id = 2025,
                        groupId = 8009,
                        spaceId = 1026,
                        houses = null
                ),
                Property(
                        id = 2026,
                        groupId = 8009,
                        spaceId = 1036,
                        houses = null
                ),
                Property(
                        id = 2027,
                        groupId = 8010,
                        spaceId = 1013,
                        houses = null
                ),
                Property(
                        id = 2028,
                        groupId = 8010,
                        spaceId = 1029,
                        houses = null,
                )
        ),
        val piles: List<Pile> = listOf<Pile>(
                Pile(
                        id = 901,
                        name = "Chance",
                        spaceIds = setOf(1008, 1023, 1037)
                ),
                Pile(
                        id = 902,
                        name = "Community Chest",
                        spaceIds = setOf(1003, 1018, 1034)
                ),
        ),
        val cards: List<Card> = listOf<Card>(
                Card(
                        id = 901001,
                        pileId = 901,
                        message = "Advance to Go (Chance)",
                        cardType = CardType.ADVANCE_TO_SPACE,
                        spaceId = 1001
                ),
                Card(
                        id = 901002,
                        pileId = 901,
                        message = "Advance to Illinois Ave.",
                        cardType = CardType.ADVANCE_TO_SPACE,
                        spaceId = 1025
                ),
                Card(
                        id = 901003,
                        pileId = 901,
                        message = "Advance to St. Charles Pl",
                        cardType = CardType.ADVANCE_TO_SPACE,
                        spaceId = 1012
                ),
                Card(
                        id = 901004,
                        pileId = 901,
                        message = "Advance token to nearest Utility",
                        cardType = CardType.ADVANCE_TO_NEAREST_PROPERTY_GROUP,
                        groupId = 8010
                ),
                Card(
                        id = 901005,
                        pileId = 901,
                        message = "Advance token to the nearest RR (1)",
                        cardType = CardType.ADVANCE_TO_NEAREST_PROPERTY_GROUP,
                        groupId = 8009
                ),
                Card(
                        id = 901006,
                        pileId = 901,
                        message = "Advance token to the nearest RR (2)",
                        cardType = CardType.ADVANCE_TO_NEAREST_PROPERTY_GROUP,
                        groupId = 8009
                ),
                Card(
                        id = 901007,
                        pileId = 901,
                        message = "Bank pays you dividend of $50",
                        cardType = CardType.CASH_FROM_BANK,
                        cash = 50
                ),
                Card(
                        id = 901008,
                        pileId = 901,
                        message = "Get out of Jail Free – This card may be kept until needed, or traded/sold (Chance)",
                        cardType = CardType.GET_OUT_OF_JAIL_FREE
                ),
                Card(
                        id = 901009,
                        pileId = 901,
                        message = "Go Back Three 3 Spaces.",
                        cardType = CardType.MOVE_SPACES,
                        spaceCount = -3
                ),
                Card(
                        id = 901010,
                        pileId = 901,
                        message = "Go to Jail. Go directly to Jail. Do not pass GO, do not collect $200.",
                        cardType = CardType.GOTO_JAIL
                ),

                Card(
                        id = 901011,
                        pileId = 901,
                        message = "Make general repairs on all your property. For each house pay $25",
                        cardType = CardType.HOUSE_REPAIR,
                        repairCostPerHouse = 25
                ),
                Card(
                        id = 901012,
                        pileId = 901,
                        message = "Pay poor tax of $15",
                        cardType = CardType.CASH_TO_BANK,
                        cash = 15
                ),
                Card(
                        id = 901013,
                        pileId = 901,
                        message = "Take a trip to Reading RR",
                        cardType = CardType.ADVANCE_TO_SPACE,
                        spaceId = 1006
                ),
                Card(
                        id = 901014,
                        pileId = 901,
                        message = "Take a walk on the Boardwalk – Advance token to Boardwalk",
                        cardType = CardType.ADVANCE_TO_SPACE,
                        spaceId = 1040
                ),
                Card(
                        id = 901015,
                        pileId = 901,
                        message = "You have been elected Chairman of the Board – Pay each player $50",
                        cardType = CardType.CASH_TO_OTHER_PLAYERS,
                        cash = 50
                ),
                Card(
                        id = 901016,
                        pileId = 901,
                        message = "Your building loan matures. Collect $150",
                        cardType = CardType.CASH_FROM_BANK,
                        cash = 150
                ),
                Card(
                        id = 901017,
                        pileId = 901,
                        message = "You have won a crossword competition - Collect $100",
                        cardType = CardType.CASH_FROM_BANK,
                        cash = 100
                ),
                Card(
                        id = 902001,
                        pileId = 902,
                        message = "Advance to Go (Community Chest)",
                        cardType = CardType.ADVANCE_TO_SPACE,
                        spaceId = 1001
                ),
                Card(
                        id = 902002,
                        pileId = 902,
                        message = "Bank error in your favor – Collect $200",
                        cardType = CardType.CASH_FROM_BANK,
                        cash = 200
                ),
                Card(
                        id = 902003,
                        pileId = 902,
                        message = "Doctor's fees – Pay $50",
                        cardType = CardType.CASH_TO_BANK,
                        cash = 50
                ),
                Card(
                        id = 902004,
                        pileId = 902,
                        message = "From sale of stock you get $50",
                        cardType = CardType.CASH_FROM_BANK,
                        cash = 50
                ),
                Card(
                        id = 902005,
                        pileId = 902,
                        message = "Get out of Jail Free – This card may be kept until needed, or traded/sold",
                        cardType = CardType.GET_OUT_OF_JAIL_FREE
                ),
                Card(
                        id = 902006,
                        pileId = 902,
                        message = "Go directly to jail. Do not pass Go, Do not collect \$200.",
                        cardType = CardType.GOTO_JAIL
                ),
                Card(
                        id = 902007,
                        pileId = 902,
                        message = "Grand Opera Night – Collect $50 from every player for opening night seats",
                        cardType = CardType.CASH_FROM_OTHER_PLAYERS,
                        cash = 50
                ),
                Card(
                        id = 902008,
                        pileId = 902,
                        message = "Holiday Fund matures - Receive $100",
                        cardType = CardType.CASH_FROM_BANK,
                        cash = 100
                ),
                Card(
                        id = 902009,
                        pileId = 902,
                        message = "Income tax refund – Collect $20",
                        cardType = CardType.CASH_FROM_BANK,
                        cash = 20
                ),
                Card(
                        id = 902010,
                        pileId = 902,
                        message = "It is your birthday - Collect $10 from each player",
                        cardType = CardType.CASH_FROM_OTHER_PLAYERS,
                        cash = 10
                ),
                Card(
                        id = 902011,
                        pileId = 902,
                        message = "Life insurance matures – Collect $100",
                        cardType = CardType.CASH_FROM_BANK,
                        cash = 100
                ),
                Card(
                        id = 902012,
                        pileId = 902,
                        message = "Pay hospital fees of $100",
                        cardType = CardType.CASH_TO_BANK,
                        cash = 100
                ),
                Card(
                        id = 902013,
                        pileId = 902,
                        message = "Pay school fees of $150",
                        cardType = CardType.CASH_TO_BANK,
                        cash = 150
                ),
                Card(
                        id = 902014,
                        pileId = 902,
                        message = "Receive $25 consultancy fee",
                        cardType = CardType.CASH_FROM_BANK,
                        cash = 25
                ),
                Card(
                        id = 902015,
                        pileId = 902,
                        message = "You are assessed for street repairs – $40 per house",
                        cardType = CardType.HOUSE_REPAIR,
                        repairCostPerHouse = 40
                ),
                Card(
                        id = 902016,
                        pileId = 902,
                        message = "You have won second prize in a beauty contest – Collect $10",
                        cardType = CardType.CASH_FROM_BANK,
                        cash = 10
                ),
                Card(
                        id = 902017,
                        pileId = 902,
                        message = "You inherit $100",
                        cardType = CardType.CASH_FROM_BANK,
                        cash = 100
                )
        )

)