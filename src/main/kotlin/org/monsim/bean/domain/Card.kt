package org.monsim.bean.domain

import org.monsim.bean.type.CardType

/** A card from Community Chest or Chance */
data class Card(
        val id: Int,
        val cardType: CardType = CardType.DO_NOTHING,
        val pileId: Int = 1,
        val message: String = "do nothing",

        val cash: Int? = null, //always nonNegative if not null
        val groupId: Int? = null,
        val spaceId: Int? = null,
        val spaceCount: Int? = null, //e.g. -3 if the player is to move back 3 spaces
        val repairCostPerHouse: Int? = null,

        var timesUsed: Int = 0
)

