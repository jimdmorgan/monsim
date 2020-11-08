package org.monsim.api.util

import org.monsim.bean.domain.Card
import org.monsim.bean.domain.Game
import org.monsim.bean.domain.Group
import org.monsim.bean.domain.Pile
import org.monsim.bean.domain.Player
import org.monsim.bean.domain.Property
import org.monsim.bean.domain.Space
import org.monsim.bean.domain.Tax

interface GameUtil {

    fun currentPlayer(game: Game): Player
    fun currentSpace(game: Game): Space
    fun currentProperty(game: Game): Property?
    fun propertiesInGroup(group: Group, game: Game): Set<Property>
    fun groupForProperty(property: Property, game: Game): Group
    fun spaceWithId(spaceId: Int, game: Game): Space
    fun playerForId(playerId: Int?, game: Game): Player?
    fun groupForId(groupId: Int, game: Game): Group
    fun cardForId(cardId: Int, game: Game): Card
    fun propertyForSpace(space: Space, game: Game): Property?
    fun taxForSpace(space: Space, game: Game): Tax?
    fun pileForSpace(space: Space, game: Game): Pile?
    fun cardsInPile(pile: Pile, game: Game): Set<Card>

    fun playersStillInGame(game: Game): Set<Player>
    fun winner(game: Game): Player?
    fun isGameOver(game: Game): Boolean
    fun currentPlayersProperties(game: Game): Set<Property>
    fun playersProperties(player: Player, game: Game): Set<Property>
    fun propertyWithName(name: String, game: Game): Property
    fun groupWithName(name: String, game: Game): Group
    fun propertiesWithGroupName(groupName: String, game: Game): Set<Property>

}