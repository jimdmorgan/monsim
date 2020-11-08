package org.monsim.impl.util

import org.monsim.bean.domain.Game
import org.monsim.bean.domain.Space
import org.monsim.bean.type.SpaceType
import org.monsim.api.util.SpaceClassifier
import java.lang.RuntimeException

class SpaceClassifierImpl : SpaceClassifier {



    override fun classifySpaces(game: Game): Map<Int, Set<SpaceType>> {
        val spaceTypesMap = mutableMapOf<Int, Set<SpaceType>>()
        val delegate = Delegate(game)
        game.spaces.forEach {
            val space = it
            spaceTypesMap.put(space.id, spaceTypesForSpace(space, delegate))
        }
        return spaceTypesMap
    }


    private fun spaceTypesForSpace(space: Space, delegate: Delegate): Set<SpaceType> {
        val allSpaceTypes = enumValues<SpaceType>()
        val spaceTypes = allSpaceTypes.filter { delegate.isSpaceType(it, space) }.toMutableSet()
        if (spaceTypes.isEmpty()){
            spaceTypes.add(SpaceType.UNASSIGNED)
        }
        return spaceTypes
    }


    private inner class Delegate(game: Game) {
        val piles = game.piles
        val spaceIds_properties =  game.properties.map{it.spaceId}.toSet()
        val spaceIds_cards = if (piles.isEmpty()) emptySet<Integer>() else piles.map{it.spaceIds}.toSet().reduce{joined, element -> joined + element}
        val spaceIds_taxes = game.taxes.map{it.spaceId}.toSet()
        val rule = game.rule

        fun isSpaceType(spaceType: SpaceType, space: Space): Boolean {
            val spaceId = space.id
            val response: Boolean = when (spaceType) {
                SpaceType.GO_TO_JAIL -> rule.gotoJailSpaceIds.contains(spaceId)
                SpaceType.FREE -> rule.freeSpaceIds.contains(spaceId)
                SpaceType.JAIL -> rule.jailSpaceId == spaceId
                SpaceType.TAX -> spaceIds_taxes.contains(spaceId)
                SpaceType.PROPERTY -> spaceIds_properties.contains(spaceId)
                SpaceType.GO -> rule.goSpaceId == spaceId
                SpaceType.CARD -> spaceIds_cards.contains(spaceId)
                SpaceType.UNASSIGNED -> false //this is assigned only if no other spaces were assigned
                else -> throw RuntimeException("bad spaceType: ${spaceType}")
            }
            return response
        }


    }



}