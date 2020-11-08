package org.monsim.api.util

import org.monsim.bean.DiceRoll
import org.monsim.bean.domain.Game


interface RandomDiceUtil {

    fun randomRoll(seed: Long, number: Long): DiceRoll


}