package org.monsim.impl.util

import org.monsim.api.util.GameUtil
import org.monsim.api.util.Spacer
import org.monsim.bean.SpaceMove
import org.monsim.bean.domain.Game
import org.monsim.bean.domain.Group
import org.monsim.bean.domain.Space
import java.lang.RuntimeException

class SpacerImpl() : Spacer {

    private val gameUtil: GameUtil = GameUtilImpl()

    data class SpaceInfo(val spaceNumber: Int, val space: Space)

    private fun spaceToInfo(space: Space, game: Game): SpaceInfo {
        val spaceId = space.id
        val spaces = game.spaces
        val spaceNumber = spaces.indexOf(space)
        when {
            spaceNumber == -1 -> throw RuntimeException("spaceId $spaceId not found")
        }
        val spaceInfo = SpaceInfo(space = space, spaceNumber = spaceNumber)
        return spaceInfo
    }

    private fun spaceNumberToInfo(spaceNumber: Int, game: Game): SpaceInfo {
        val spaces = game.spaces
        when {
            spaceNumber < 0 || spaceNumber >= spaces.size -> throw RuntimeException("bad spaceNumber $spaceNumber")
        }
        val space = spaces[spaceNumber]
        val spaceInfo = SpaceInfo(space = space, spaceNumber = spaceNumber)
        return spaceInfo
    }

    private fun spaceInfoAfterRoll(spacesToMove: Int, game: Game): SpaceInfo {
        val currentSpaceInfo = currentSpaceInfo(game)
        val numberOfSpaces = game.spaces.size
        val newSpaceNumber = (currentSpaceInfo.spaceNumber + spacesToMove) % numberOfSpaces
        val newSpaceInfo = spaceNumberToInfo(newSpaceNumber, game)
        return newSpaceInfo
    }

    override fun calcSpaceMove(spacesToMove: Int, game: Game): SpaceMove {
        val newSpaceInfo = spaceInfoAfterRoll(spacesToMove, game)
        val isGoPassed = isGoPassed(spacesToMove, game)
        val spaceMove = SpaceMove(newSpace = newSpaceInfo.space, isGoPassed = isGoPassed)
        return spaceMove
    }

    private fun isGoPassed(spacesToMove: Int, game: Game): Boolean {
        var isGoPassed = false
        for (roll in 1..spacesToMove) {
            val newSpaceInfo = spaceInfoAfterRoll(roll, game)
            if (game.rule.goSpaceId == newSpaceInfo.space.id) {
                isGoPassed = true
            }
        }
        return isGoPassed
    }

    override fun rollNeededToGetToSpace(targetSpace: Space, game: Game): Int {
        for(roll in 1..game.spaces.size){
            val candidateSpaceInfo = spaceInfoAfterRoll(roll, game)
            if (candidateSpaceInfo.space == targetSpace){
                return roll
            }
        }
        throw RuntimeException("targetSpace (#${targetSpace.id}) not found in spaces")
    }

    override fun spaceOfNearestPropertyInGroup(group: Group, game: Game): Space {
        for(roll in 1..game.spaces.size){
            val newSpaceInfo = spaceInfoAfterRoll(roll, game)
            val property = gameUtil.propertyForSpace(newSpaceInfo.space, game)
            if (property == null) {
                continue
            }
            if (property.groupId == group.id) {
                return newSpaceInfo.space
            }
        }
        throw RuntimeException("no property found for groupId#${group.id}")
    }

    private fun currentSpaceInfo(game: Game): SpaceInfo {
        val currentSpace = gameUtil.currentSpace(game)
        val currentSpaceInfo = spaceToInfo(currentSpace, game)
        return currentSpaceInfo
    }
}


