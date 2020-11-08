package org.monsim.api.util

import org.monsim.bean.domain.Game
import org.monsim.bean.type.SpaceType

interface SpaceClassifier {

    fun classifySpaces(game: Game): Map<Int, Set<SpaceType>>
}