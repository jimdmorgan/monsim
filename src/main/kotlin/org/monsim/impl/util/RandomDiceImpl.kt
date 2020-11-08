package org.monsim.impl.util

import mu.KotlinLogging
import org.monsim.api.util.RandomDiceUtil
import org.monsim.bean.DiceRoll
import java.util.*

class RandomDiceImpl : RandomDiceUtil {

    private val log = KotlinLogging.logger {}

    companion object {
        val DICE_MIN_VALUE = 1
        val DICE_MAX_VALUE = 6
        val ROLLS_PER_SEED = 1000
        val NUMBER_OF_DICE = 2
        val seedRollsMap:MutableMap<Long, List<List<Int>>> = mutableMapOf()
        fun rollsForSeed(seed: Long):List<List<Int>> {
            if (!seedRollsMap.containsKey(seed)){
                seedRollsMap[seed] = initListForSeed(seed)
            }
            return seedRollsMap[seed]!!
        }
        private fun initListForSeed(seed: Long): List<List<Int>> {
            val random = Random(seed)
            val rolls = List(ROLLS_PER_SEED) { randomRoll(random) }
            return rolls
        }

        private fun randomRoll(random: Random): List<Int>{
            val roll = List(NUMBER_OF_DICE) { random.nextInt(DICE_MAX_VALUE) + DICE_MIN_VALUE }
            return roll
        }
    }

    override fun randomRoll(seed: Long, number: Long): DiceRoll {
        val rollsForSeed = rollsForSeed(seed)
        val index = (number % rollsForSeed.size).toInt()
        val roll = rollsForSeed[index]
        val sum = roll.sum()
        val isDoubles = roll[0] == roll[1]
        val diceRoll = DiceRoll(sum=sum, isDoubles=isDoubles)
        //val diceRoll = DiceRoll(sum=2, isDoubles=true)  //hard-code the dice roll so that X is rolled each time
        log.trace{"random roll for seed#${seed} number#$number is: ${diceRoll}"}
        return diceRoll
    }


}