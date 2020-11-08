package org.monsim.impl.checker

import org.monsim.bean.domain.Game
import org.monsim.bean.domain.Space
import org.monsim.impl.util.SpaceClassifierImpl
import org.monsim.impl.util.UniqueUtilImpl

class SpaceCheckerStore {

    abstract class Space_Abstract_Checker(private val game: Game) : AbstractCheckerStore.Abstract_Checker(game) {

        final override fun itemsToCheck(): List<Any> {
            return game.spaces
        }
    }

    abstract class Space_SingleAbstract_Checker(private val game: Game) : Space_Abstract_Checker(game) {
        val uniqueUtil = UniqueUtilImpl()
        val spaceIds = game.spaces.map{it.id}.toSet()
        val spaceTypesMap = SpaceClassifierImpl().classifySpaces(game)
        final override fun itemError(item: Any): String? {
            val space = item as Space
            var error = spaceError(space)
            if (error != null){
                error = "Bad SpaceId#${space.id}: $error"
            }
            return error
        }
        abstract fun spaceError(space: Space): String?
    }

    class Space_Aggregate_OneOrMore_Checker(val game: Game) : Space_Abstract_Checker(game) {
        override fun aggregateError(): String? {
            val error = if (game.spaces.size > 0) null else "no spaces"
            return error
        }
    }

    class Space_Type_Checker(val game: Game) : Space_SingleAbstract_Checker(game) {
        override fun spaceError(space: Space): String? {
            val spaceId = space.id
            val spaceTypes =  spaceTypesMap.get(spaceId)!!
            val size = spaceTypes.size
            val error = when (size){
                1 -> null
                0 -> "Can't determine spaceType (should have been given UNASSIGNED)"
                else -> "has multiple spaceTypes ($spaceTypes)"
            }
            return error
        }
    }

    class Space_IdsUnique_Checker(val game: Game) : Space_SingleAbstract_Checker(game) {
        val repeatedSpaceIds = uniqueUtil.repeatedInts(
                game.spaces.map{it.id}.toList())
        override fun spaceError(space: Space): String? {
            val spaceId = space.id
            val error = if (!repeatedSpaceIds.contains(spaceId)) null else
                "duplicate id '$spaceId'"
            return error
        }
    }


    class Space_NameUnique_Checker(val game: Game) : Space_SingleAbstract_Checker(game) {
        val repeatedSpaceNames = uniqueUtil.repeatedStrings(
                game.spaces.map{it.name}.toList())
        override fun spaceError(space: Space): String? {
            val name = space.name
            val error = if (!repeatedSpaceNames.contains(name)) null else
                "duplicate name '$name'"
            return error
        }
    }




}