package org.monsim.api.checker

import org.monsim.bean.domain.Game

interface MainChecker {

    fun gameErrors(game: Game): List<String>

}