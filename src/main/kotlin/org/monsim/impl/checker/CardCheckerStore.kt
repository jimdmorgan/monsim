package org.monsim.impl.checker

import org.monsim.bean.domain.Game
import org.monsim.bean.domain.Card
import org.monsim.bean.type.CardType
import org.monsim.impl.util.UniqueUtilImpl
import java.lang.RuntimeException

class CardCheckerStore {

    abstract class Card_Abstract_Checker(private val game: Game) : AbstractCheckerStore.Abstract_SingleItem_Checker(game) {
        val uniqueUtil = UniqueUtilImpl()

        final override fun itemsToCheck(): List<Any> {
            return game.cards
        }

        final override fun itemError(item: Any): String? {
            val card = item as Card
            var error = cardError(card)
            if (error != null) {
                error = "Bad Card#${card.id}: ${error}"
            }
            return error
        }

        abstract fun cardError(card: Card): String?

    }

    class Card_TimesUsed_Checker(val game: Game) : Card_Abstract_Checker(game) {
        override fun cardError(card: Card): String? {
            val timesUsed = card.timesUsed
            val error = if (timesUsed >= 0) null else "timesUsed '$timesUsed' is negative"
            return error
        }
    }


    class Card_Cash_Checker(val game: Game) : Card_Abstract_Checker(game) {
        private val cashCardTypes = setOf(
                CardType.CASH_FROM_BANK,
                CardType.CASH_FROM_OTHER_PLAYERS,
                CardType.CASH_TO_BANK,
                CardType.CASH_TO_OTHER_PLAYERS
        )

        override fun cardError(card: Card): String? {
            val cash = card.cash
            val isSupposedToHaveCash = cashCardTypes.contains(card.cardType)
            val hasCash = (cash != null)
            val error = when {
                !isSupposedToHaveCash && !hasCash -> null
                isSupposedToHaveCash && !hasCash -> "should have cash value"
                !isSupposedToHaveCash && hasCash -> "shouldn't have cash value"
                isSupposedToHaveCash && hasCash -> errorIfBadCash(cash!!)
                else -> throw RuntimeException("logic error in when")
            }
            return error
        }

        private fun errorIfBadCash(cash: Int): String? {
            return if (cash >= 0) null
            else "cash '$cash' can't be negative"
        }
    }

    class Card_RepairCost_Checker(val game: Game) : Card_Abstract_Checker(game) {
        override fun cardError(card: Card): String? {
            val repairCost = card.repairCostPerHouse
            val isSupposedToHaveRepairCost = card.cardType == CardType.HOUSE_REPAIR
            val hasRepairCost = (repairCost != null)
            val error = when {
                !isSupposedToHaveRepairCost && !hasRepairCost -> null
                isSupposedToHaveRepairCost && !hasRepairCost -> "should have repairCost value"
                !isSupposedToHaveRepairCost && hasRepairCost -> "shouldn't have repairCost value"
                isSupposedToHaveRepairCost && hasRepairCost -> errorIfBadRepairCost(repairCost!!)
                else -> throw RuntimeException("logic error in when")
            }
            return error
        }

        private fun errorIfBadRepairCost(repairCost: Int): String? {
            return if (repairCost >= 0) null
            else "repairCost '$repairCost' can't be negative"
        }
    }

    class Card_SpaceCount_Checker(val game: Game) : Card_Abstract_Checker(game) {
        override fun cardError(card: Card): String? {
            val spaceCount = card.spaceCount
            val isSupposedToHaveSpaceCount = card.cardType == CardType.MOVE_SPACES
            val hasSpaceCount = (spaceCount != null)
            val error = when {
                !isSupposedToHaveSpaceCount && !hasSpaceCount -> null
                isSupposedToHaveSpaceCount && !hasSpaceCount -> "should have spaceCount value"
                !isSupposedToHaveSpaceCount && hasSpaceCount -> "shouldn't have spaceCount value"
                isSupposedToHaveSpaceCount && hasSpaceCount -> null
                else -> throw RuntimeException("logic error in when")
            }
            return error
        }
    }

    class Card_SpaceIds_Checker(val game: Game) : Card_Abstract_Checker(game) {
        val validSpaceIds = game.spaces.map { it.id }.toSet()
        override fun cardError(card: Card): String? {
            val spaceId = card.spaceId
            val isSupposedToHaveSpaceId = (card.cardType == CardType.ADVANCE_TO_SPACE)
            val hasSpaceId = (spaceId != null)
            val error = when {
                !isSupposedToHaveSpaceId && !hasSpaceId -> null
                isSupposedToHaveSpaceId && !hasSpaceId -> "should have a spaceId"
                !isSupposedToHaveSpaceId && hasSpaceId -> "shouldn't have a spaceId"
                isSupposedToHaveSpaceId && hasSpaceId -> errorIfBadSpaceId(spaceId!!)
                else -> throw RuntimeException("logic error in when")
            }
            return error
        }

        private fun errorIfBadSpaceId(spaceId: Int): String? {
            return if (validSpaceIds.contains(spaceId)) null
            else "no such spaceId '$spaceId'"
        }
    }


    class Card_GroupIds_Checker(val game: Game) : Card_Abstract_Checker(game) {
        val validGroupIds = game.groups.map { it.id }.toSet()
        override fun cardError(card: Card): String? {
            val groupId = card.groupId
            val isSupposedToHaveGroupId = (card.cardType == CardType.ADVANCE_TO_NEAREST_PROPERTY_GROUP)
            val hasGroupId = (groupId != null)
            val error = when {
                !isSupposedToHaveGroupId && !hasGroupId -> null
                isSupposedToHaveGroupId && !hasGroupId -> "should have a groupId"
                !isSupposedToHaveGroupId && hasGroupId -> "shouldn't have a groupId"
                isSupposedToHaveGroupId && hasGroupId -> errorIfBadGroupId(groupId!!)
                else -> throw RuntimeException("logic error in when")
            }
            return error
        }

        private fun errorIfBadGroupId(groupId: Int): String? {
            return if (validGroupIds.contains(groupId)) null
            else "no such groupId '$groupId'"
        }
    }


    class Card_IdsUnique_Checker(val game: Game) : Card_Abstract_Checker(game) {
        val repeatedCardIds = uniqueUtil.repeatedInts(
                game.cards.map { it.id }.toList())

        override fun cardError(card: Card): String? {
            val cardId = card.id
            val error = if (!repeatedCardIds.contains(cardId)) null else
                "duplicate id '$cardId'"
            return error
        }
    }

    class Card_MessageUnique_Checker(val game: Game) : Card_Abstract_Checker(game) {
        val repeatedMessages = uniqueUtil.repeatedStrings(
                game.cards.map { it.message }.toList())

        override fun cardError(card: Card): String? {
            val message = card.message
            val error = if (!repeatedMessages.contains(message)) null else
                "duplicate message '$message'"
            return error
        }
    }

    class Card_Pile_Checker(val game: Game) : Card_Abstract_Checker(game) {
        val validPileIds = game.piles.map { it.id }.toSet()

        override fun cardError(card: Card): String? {
            val pileId = card.pileId
            val error = if (validPileIds.contains(pileId)) null else
                "invalid pileId '${pileId}'"
            return error
        }
    }
}

