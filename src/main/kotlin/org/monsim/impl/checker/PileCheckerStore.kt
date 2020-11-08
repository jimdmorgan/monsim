package org.monsim.impl.checker

import org.monsim.bean.domain.Game
import org.monsim.bean.domain.Pile
import org.monsim.impl.util.UniqueUtilImpl

class PileCheckerStore {

    abstract class Pile_Abstract_Checker(private val game: Game) : AbstractCheckerStore.Abstract_SingleItem_Checker(game) {
        val uniqueUtil = UniqueUtilImpl()
        val pileIds = game.piles.map{it.id}.toSet()

        final override fun itemsToCheck(): List<Any> {
            return game.piles
        }

        final override fun itemError(item: Any): String? {
            val pile = item as Pile
            var error = pileError(pile)
            if (error != null){
                error = "Bad Pile#${pile.id}: $error"
            }
            return error
        }

        abstract fun pileError(pile: Pile): String?

    }

    class Pile_SpaceIds_Checker(val game: Game) : Pile_Abstract_Checker(game) {
        val validSpaceIds = game.spaces.map{it.id}.toSet()
        override fun pileError(pile: Pile): String? {
            val invalidSpaceIds = pile.spaceIds.filter{!validSpaceIds.contains(it)}.toSet()
            val error = if (invalidSpaceIds.isEmpty()) null else
                "invalid spaceIds '$invalidSpaceIds'"
            return error
        }
    }


    class Pile_IdsUnique_Checker(val game: Game) : Pile_Abstract_Checker(game) {
        val repeatedPileIds = uniqueUtil.repeatedInts(
                game.piles.map{it.id}.toList())
        override fun pileError(pile: Pile): String? {
            val pileId = pile.id
            val error = if (!repeatedPileIds.contains(pileId)) null else
                "duplicate id '$pileId'"
            return error
        }
    }

    class Pile_NameUnique_Checker(val game: Game) : Pile_Abstract_Checker(game) {
        val repeatedPileNames = uniqueUtil.repeatedStrings(
                game.piles.map{it.name}.toList())
        override fun pileError(pile: Pile): String? {
            val pileName = pile.name
            val error = if (!repeatedPileNames.contains(pileName)) null else
                "duplicate name '$pileName'"
            return error
        }
    }






}