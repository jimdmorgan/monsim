package org.monsim.impl.checker

import mu.KotlinLogging
import org.monsim.bean.domain.Game
import org.monsim.api.checker.ItemChecker
import org.monsim.api.checker.MainChecker

class MainCheckerImpl() : MainChecker {

    private val log = KotlinLogging.logger {}

    override fun gameErrors(game: Game): List<String> {
        val itemCheckers = itemCheckers(game)
        return gameErrorsForItemCheckers(game, itemCheckers)
    }

    fun gameErrorsForItemCheckers(game: Game, itemCheckers: List<ItemChecker>): List<String>{
        val errors = mutableListOf<String>()
        itemCheckers.forEach {
            val checker = it
            val items = checker.itemsToCheck()
            items.forEach {
                val item = it
                val error = checker.itemError(item)
                if (error != null){
                    errors.add(error)
                }
            }
        }
        log.trace{"${errors.size} error(s) were generated from ${itemCheckers.size} itemChecker(s)"}
        return errors
    }

    fun itemCheckers(game: Game): List<ItemChecker>{
        val itemCheckers: List<ItemChecker> = listOf(
                CardCheckerStore.Card_Cash_Checker(game),
                CardCheckerStore.Card_GroupIds_Checker(game),
                CardCheckerStore.Card_IdsUnique_Checker(game),
                CardCheckerStore.Card_MessageUnique_Checker(game),
                CardCheckerStore.Card_Pile_Checker(game),
                CardCheckerStore.Card_RepairCost_Checker(game),
                CardCheckerStore.Card_SpaceCount_Checker(game),
                CardCheckerStore.Card_SpaceIds_Checker(game),
                CardCheckerStore.Card_TimesUsed_Checker(game),
                GroupCheckerStore.Group_BuyPrice_Checker(game),
                GroupCheckerStore.Group_HousePrice_Checker(game),
                GroupCheckerStore.Group_IdsUnique_Checker(game),
                GroupCheckerStore.Group_NameUnique_Checker(game),
                GroupCheckerStore.Group_Rent_Checker(game),
                PileCheckerStore.Pile_SpaceIds_Checker(game),
                PileCheckerStore.Pile_IdsUnique_Checker(game),
                PileCheckerStore.Pile_NameUnique_Checker(game),
                PlayerCheckerStore.Player_Aggregate_OneOrMore_Checker(game),
                PlayerCheckerStore.Player_Aggregate_AtLeastOnePlaying_Checker(game),
                PlayerCheckerStore.Player_CreditorId_Checker(game),
                PlayerCheckerStore.Player_FreeBailCards_Checker(game),
                PlayerCheckerStore.Player_IdsUnique_Checker(game),
                PlayerCheckerStore.Player_RollsInJail_Checker(game),
                PlayerCheckerStore.Player_SpaceId_Checker(game),
                PropertyCheckerStore.Property_GroupId_Checker(game),
                PropertyCheckerStore.Property_Houses_Checker(game),
                PropertyCheckerStore.Property_IdsUnique_Checker(game),
                PropertyCheckerStore.Property_OwnerId_Checker(game),
                PropertyCheckerStore.Property_Schedule_Checker(game),
                PropertyCheckerStore.Property_SpaceId_Checker(game),
                RuleCheckerStore.Rule_Bail_Checker(game),
                RuleCheckerStore.Rule_CashForPassingGo_Checker(game),
                RuleCheckerStore.Rule_FreeSpaceIds_Checker(game),
                RuleCheckerStore.Rule_GoSpaceId_Checker(game),
                RuleCheckerStore.Rule_JailSpaceId_Checker(game),
                RuleCheckerStore.Rule_GotoJailSpaceIds_Checker(game),
                RuleCheckerStore.Rule_MaxDoubles_Checker(game),
                RuleCheckerStore.Rule_MaxRollsInJail_Checker(game),
                RuleCheckerStore.Rule_NoHousesRentIncreasePercent_Checker(game),
                RuleCheckerStore.Rule_MortgagePercentPenalty_Checker(game),
                RuleCheckerStore.Rule_UnmortgagePercentPenalty_Checker(game),
                RuleCheckerStore.Rule_HouseSellBackPercent_Checker(game),
                SpaceCheckerStore.Space_Aggregate_OneOrMore_Checker(game),
                SpaceCheckerStore.Space_NameUnique_Checker(game),
                SpaceCheckerStore.Space_Type_Checker(game),
                SpaceCheckerStore.Space_IdsUnique_Checker(game),
                TaxCheckerStore.Tax_Amount_Checker(game),
                TaxCheckerStore.Tax_SpaceId_Checker(game),
                TurnCheckerStore.Turn_DoublesRolled_Checker(game),
                TurnCheckerStore.Turn_Roll_Checker(game),
                TurnCheckerStore.Turn_PlayerId_Checker(game)

        )
        return itemCheckers
    }


}