package org.monsim.impl.checker

import org.monsim.bean.domain.Game
import org.monsim.bean.domain.Tax
import org.monsim.api.checker.ItemChecker

class TaxCheckerStore {

    abstract class Tax_Abstract_Checker(private val game: Game) : AbstractCheckerStore.Abstract_SingleItem_Checker(game) {

        val spaceIds = game.spaces.map{it.id}.toSet()

        final override fun itemsToCheck(): List<Any> {
            return game.taxes
        }

        final override fun itemError(item: Any): String? {
            val tax = item as Tax
            var error = taxError(tax)
            if (error != null){
                error = "Bad tax '$tax': $error"
            }
            return error
        }

        abstract fun taxError(tax: Tax): String?
    }

    class Tax_SpaceId_Checker(val game: Game) : Tax_Abstract_Checker(game) {

        override fun taxError(tax: Tax): String? {
            val spaceId = tax.spaceId
            return if (spaceIds.contains(spaceId)) null else
                "spaceId '$spaceId' doesn't exist"
        }
    }


    class Tax_Amount_Checker(val game: Game) : Tax_Abstract_Checker(game) {

        override fun taxError(tax: Tax): String? {
            val amount = tax.amount
            return if (amount >= 0) null else
                "amount '$amount' is negative"
        }
    }

}