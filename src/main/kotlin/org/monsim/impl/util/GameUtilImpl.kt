package org.monsim.impl.util

import org.monsim.api.util.GameUtil
import org.monsim.bean.Constants
import org.monsim.bean.domain.Card
import org.monsim.bean.domain.Game
import org.monsim.bean.domain.Group
import org.monsim.bean.domain.Pile
import org.monsim.bean.domain.Player
import org.monsim.bean.domain.Property
import org.monsim.bean.domain.Space
import org.monsim.bean.domain.Tax
import java.lang.RuntimeException

class GameUtilImpl : GameUtil {



    override fun currentSpace(game: Game): Space {
        val player = currentPlayer(game)
        val space = spaceWithId(player.spaceId, game)
        return space
    }

    override fun currentProperty(game: Game): Property? {
        val space = currentSpace(game)
        val property = propertyForSpace(space, game)
        return property
    }

    override fun currentPlayer(game: Game): Player {
        return playerForId(game.turn.playerId, game)!!
    }

    override fun propertiesInGroup(group: Group, game: Game): Set<Property> {
        val properties = game.properties.filter { it.groupId == group.id }.toSet()
        return properties
    }

    override fun groupForProperty(property: Property, game: Game): Group {
        val groups = game.groups.filter { it.id == property.groupId }.toSet()
        if (groups.size != 1) {
            throw RuntimeException("can't get group for property: ${property.id}")
        }
        return groups.first()
    }

    override fun spaceWithId(spaceId: Int, game: Game): Space {
        val spaces = game.spaces.filter { it.id == spaceId }.toSet()
        if (spaces.size != 1) {
            throw RuntimeException("can't get space for spaceId#${spaceId}")
        }
        return spaces.first()
    }

    override fun playerForId(playerId: Int?, game: Game): Player? {
        if (playerId == Constants.BANK_ID) {
            return Constants.BANK
        }
        val players = game.players.filter { it.id == playerId }.toSet()
        if (players.size != 1) {
            throw RuntimeException("can't get player for id#${playerId}")
        }
        return players.first()
    }

    override fun groupForId(groupId: Int, game: Game): Group {
        val groups = game.groups.filter { it.id == groupId }.toSet()
        if (groups.size != 1) {
            throw RuntimeException("can't get group for id#${groupId}")
        }
        return groups.first()
    }

    override fun cardForId(cardId: Int, game: Game): Card {
        val cards = game.cards.filter { it.id == cardId }.toSet()
        val card = when (cards.size) {
            1 -> cards.first()
            0 -> throw RuntimeException("No card with id#${cardId}")
            else -> throw RuntimeException("more than 1 card has the same id of ${cardId}")
        }
        return card
    }

    override fun propertyForSpace(space: Space, game: Game): Property? {
        val spaceId = space.id
        val properties = game.properties.filter { it.spaceId == spaceId }.toSet()
        val property = when (properties.size) {
            1 -> properties.first()
            0 -> null
            else -> throw RuntimeException("more than 1 property refers to spaceId ${spaceId}")
        }
        return property
    }

    override fun taxForSpace(space: Space, game: Game): Tax? {
        val spaceId = space.id
        val taxes = game.taxes.filter { it.spaceId == spaceId }.toSet()
        val tax = when (taxes.size) {
            0 -> null
            1 -> taxes.first()
            else -> throw RuntimeException("more than 1 tax refers to spaceId ${spaceId}")
        }
        return tax
    }


    override fun cardsInPile(pile: Pile, game: Game): Set<Card> {
        val cards = game.cards.filter{it.pileId == pile.id}.toSet()
        return cards
    }

    override fun pileForSpace(space: Space, game: Game): Pile? {
        val spaceId = space.id
        val piles = game.piles.filter { it.spaceIds.contains(spaceId) }
        val pile = when (piles.size) {
            0 -> null
            1 -> piles.first()
            else -> throw RuntimeException("More than 1 pile refers to spaceId#${spaceId}")
        }
        return pile
    }

    override fun winner(game: Game): Player? {
        val players = playersStillInGame(game)
        val winner = when(players.size){
            0 -> throw RuntimeException("no players in game (or all players have lost set to true)")
            1 -> players.first()
            else -> null
        }
        return winner
    }

    override fun isGameOver(game: Game): Boolean {
        return winner(game) != null
    }

    override fun playersStillInGame(game: Game): Set<Player> {
        val players = game.players.filter{!it.lost}.toSet()
        return players
    }

    override fun currentPlayersProperties(game: Game): Set<Property> {
        val player = currentPlayer(game)
        return playersProperties(player, game)
    }

    override fun playersProperties(player: Player, game: Game): Set<Property> {
        val properties = game.properties.filter{it.ownerId == player.id}.toSet()
        return properties
    }

    override fun propertyWithName(name: String, game: Game): Property {
        val space: Space = spaceWithName(name, game)
        val properties = game.properties.filter{it.spaceId == space.id}.toSet()
        val property = when (properties.size){
            1 -> properties.first()
            0 -> throw RuntimeException("no property refers to SpaceId#${space.id}")
            else -> throw RuntimeException("more than 1 property refers to SpaceId#${space.id}")
        }
        return property
    }

    private fun spaceWithName(name: String, game: Game): Space {
        val spaces = game.spaces.filter{it.name.equals(name)}.toSet()
        val space = when (spaces.size) {
            1 -> spaces.first()
            0 -> throw RuntimeException("No space with name '${name}'")
            else -> throw RuntimeException("more than 1 space has the same name of ${name}")
        }
        return space
    }

    override fun groupWithName(name: String, game: Game): Group {
        val groups = game.groups.filter{it.name.equals(name)}.toSet()
        val group = when (groups.size) {
            1 -> groups.first()
            0 -> throw RuntimeException("No group with name '${name}'")
            else -> throw RuntimeException("more than 1 group has the same name of ${name}")
        }
        return group
    }

    override fun propertiesWithGroupName(groupName: String, game: Game): Set<Property> {
        val group = groupWithName(groupName, game)
        return propertiesInGroup(group, game)
    }
}