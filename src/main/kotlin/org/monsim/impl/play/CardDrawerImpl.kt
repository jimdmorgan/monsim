package org.monsim.impl.play

import mu.KotlinLogging
import org.monsim.api.play.CardDrawer
import org.monsim.bean.domain.Card
import org.monsim.bean.domain.Game
import org.monsim.bean.domain.Pile
import org.monsim.bean.type.CardType
import org.monsim.impl.util.GameUtilImpl
import java.util.*

class CardDrawerImpl : CardDrawer {

    private val gameUtil = GameUtilImpl()
    private val log = KotlinLogging.logger {}

    override fun drawCard(pile: Pile, game: Game): Card {
        var card = drawCardDelegate(pile, game)
        log.debug { "Card#${card.id} was drawn from the '${pile.name}' pile (pile#${pile.id}).  It has now been used ${card.timesUsed} time(s). Card:'${card.message}'" }
        return card
    }

    fun drawCardDelegate(pile: Pile, game: Game): Card {
        val cards = gameUtil.cardsInPile(pile, game)
        if (cards.isEmpty()){
            return defaultCardInPile(pile)
        }
        val minTimesUsed = cards.minOf { it.timesUsed }
        val random = Random(game.rule.seed)
        val leastUsedCards = cards.filter { it.timesUsed == minTimesUsed }.toList().shuffled(random)
        val card = leastUsedCards[0]
        card.timesUsed++
        return card
    }

    fun defaultCardInPile(pile: Pile): Card {
        var card = Card(
                id = -1,
                cardType = CardType.DO_NOTHING,
                pileId = pile.id,
                message = "Do nothing. (Default card when there are no cards in pile)",
                timesUsed = 0
        )
        return card
    }


}