package org.monsim.bean


data class RollLogic(
        val isJailed: Boolean = false,
        val isFreed: Boolean = false,
        val incrementDoublesRolled: Boolean = false,
        val incrementRollsInJail: Boolean = false,
        val isRollingDone: Boolean = false,
        val isMoved: Boolean = false,
        var rollSum: Int = 7,
)

