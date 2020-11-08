package org.monsim.api.util

import org.monsim.bean.domain.Game
import org.monsim.bean.domain.Group
import org.monsim.bean.DiceRoll
import org.monsim.bean.SpaceMove
import org.monsim.bean.domain.Space


interface Spacer {

    fun calcSpaceMove(spacesToMove: Int, game: Game): SpaceMove
    fun rollNeededToGetToSpace(targetSpace: Space, game: Game): Int
    fun spaceOfNearestPropertyInGroup(group: Group, game: Game): Space


}