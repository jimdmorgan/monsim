package org.monsim.impl.checker

import org.monsim.bean.domain.Game
import org.monsim.bean.domain.Rule
import org.monsim.api.checker.ItemChecker

class RuleCheckerStore {

    abstract class Rule_Abstract_Checker(private val game: Game) : AbstractCheckerStore.Abstract_SingleItem_Checker(game) {

        val spaceIds = game.spaces.map{it.id}.toSet()

        final override fun itemsToCheck(): List<Any> {
            return listOf(game.rule)
        }

        final override fun itemError(item: Any): String? {
            val rule = item as Rule
            var error = ruleError(rule)
            if (error != null){
                error = "Bad rule: $error"
            }
            return error
        }

        abstract fun ruleError(rule: Rule): String?
    }


    abstract class Rule_SpaceId_Checker(
            private val game: Game,
            private val fieldName: String,
            private val spaceIdGetter: (rule: Rule) -> Int
    ) : Rule_Abstract_Checker(game) {
        override fun ruleError(rule: Rule): String? {
            val spaceId = spaceIdGetter(rule)
            return if (spaceIds.contains(spaceId)) null else
                "$fieldName refers to a nonExistant spaceId '$spaceId'"
        }
    }


    class Rule_GoSpaceId_Checker(val game: Game) : Rule_SpaceId_Checker(game, "goSpaceId", {it.goSpaceId}) {}
    class Rule_JailSpaceId_Checker(val game: Game) : Rule_SpaceId_Checker(game, "jailSpaceId", {it.jailSpaceId}) {}

    abstract class Rule_SpaceIds_Checker(
            private val game: Game,
            private val fieldName: String,
            private val spaceIdGetter: (rule: Rule) -> Set<Int>
    ) : Rule_Abstract_Checker(game) {
        override fun ruleError(rule: Rule): String? {
            val givenSpaceIds = spaceIdGetter(rule)
            val invalidSpaceIds = givenSpaceIds.filter { !spaceIds.contains(it) }.toSet()
            return if (invalidSpaceIds.isEmpty()) null else
                "$fieldName refers to a nonExistant spaceIds '$invalidSpaceIds'"
        }
    }

    class Rule_GotoJailSpaceIds_Checker(val game: Game) : Rule_SpaceIds_Checker(game, "gotoJailSpaceIds", {it.gotoJailSpaceIds}) {}
    class Rule_FreeSpaceIds_Checker(val game: Game) : Rule_SpaceIds_Checker(game, "freeSpaceIds", {it.freeSpaceIds}) {}

    abstract class Rule_NonNegative_Checker(
            private val game: Game,
            private val fieldName: String,
            private val fieldGetter: (rule: Rule) -> Int
    ) : Rule_Abstract_Checker(game) {
        override fun ruleError(rule: Rule): String? {
            val value = fieldGetter(rule)
            return if (value >= 0) null else
                "$fieldName is negative '$value'"
        }
    }

    class Rule_MaxDoubles_Checker(val game: Game) : Rule_NonNegative_Checker(game, "maxDoubles", {it.maxDoubles}){}
    class Rule_MaxRollsInJail_Checker(val game: Game) : Rule_NonNegative_Checker(game, "maxRollsInJail", {it.maxRollsInJail}){}
    class Rule_Bail_Checker(val game: Game) : Rule_NonNegative_Checker(game, "bail", {it.bail}){}
    class Rule_CashForPassingGo_Checker(val game: Game) : Rule_NonNegative_Checker(game, "cashForPassingGo", {it.cashForPassingGo}){}
    class Rule_MortgagePercentPenalty_Checker(val game: Game) : Rule_NonNegative_Checker(game, "mortgagePercentPenalty", {it.mortgagePercent}){}
    class Rule_UnmortgagePercentPenalty_Checker(val game: Game) : Rule_NonNegative_Checker(game, "unmortgagePercentPenalty", {it.unmortgagePercent}){}
    class Rule_NoHousesRentIncreasePercent_Checker(val game: Game) : Rule_NonNegative_Checker(game, "noHousesRentIncreasePercent", {it.monopolyWithoutHousesRentPercent}){}
    class Rule_HouseSellBackPercent_Checker(val game: Game) : Rule_NonNegative_Checker(game, "houseSellBackPercent", {it.houseSellBackPercent}){}


}