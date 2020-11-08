package org.monsim.bean.domain

import org.monsim.bean.DefaultGameValues

data class Game(
        val rule: Rule = Rule(),
        val spaces: List<Space> = DefaultGameValues().spaces,
        val players: List<Player> = DefaultGameValues().players,
        val groups: List<Group> = DefaultGameValues().groups,
        val properties: List<Property> = DefaultGameValues().properties,
        val turn: Turn = Turn(),
        val piles: List<Pile> = DefaultGameValues().piles,
        val cards: List<Card> = DefaultGameValues().cards,
        val taxes: List<Tax> = DefaultGameValues().taxes,
)

