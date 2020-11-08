package org.monsim.bean.domain



data class Turn(
        var playerId: Int = 501, //the playerId of the player whose turn it is
        var isRollingDone: Boolean = false, //true once a player has rolled and has no more rolls left this turn
        var doublesRolled: Int = 0, //the number of times doubles have been rolled by the current player this turn
        var rollSum: Int = 7, //the sum of the dice for the last dice roll
        var totalRolls: Long = 0, //the total number of rolls in the game.  Could go infinitely high
)

