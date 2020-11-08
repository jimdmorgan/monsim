package org.monsim.api.ai

import org.monsim.bean.domain.Game

interface AiRunner {

    /** Automatically plays a game using artificial intelligence
     * The players automatically roll dice, buy property, and buy houses
     */
    fun autoPlayGameUsingAi(game: Game)

}