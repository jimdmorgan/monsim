package org.monsim.impl.play

import mu.KotlinLogging
import org.monsim.api.play.MultiPlayerPayer
import org.monsim.api.play.Jailer
import org.monsim.api.play.Renter
import org.monsim.api.accounting.CashAccounter
import org.monsim.api.accounting.FreeBailCardAccounter
import org.monsim.api.accounting.PropertyAccounter
import org.monsim.api.play.GoCollector
import org.monsim.api.play.FreeParker
import org.monsim.api.play.Repairer
import org.monsim.api.play.Taxer
import org.monsim.api.play.Mover
import org.monsim.api.play.CardDrawer
import org.monsim.api.util.GameUtil
import org.monsim.api.util.SpaceClassifier
import org.monsim.api.util.Spacer
import org.monsim.bean.Bill
import org.monsim.bean.SpaceMove
import org.monsim.bean.domain.Card
import org.monsim.bean.domain.Game
import org.monsim.bean.domain.Group
import org.monsim.bean.domain.Player
import org.monsim.bean.domain.Space
import org.monsim.bean.type.CardType
import org.monsim.bean.type.SpaceType
import org.monsim.impl.util.GameUtilImpl
import org.monsim.impl.util.SpaceClassifierImpl
import org.monsim.impl.util.SpacerImpl
import org.monsim.impl.accounting.CashAccounterImpl
import org.monsim.impl.accounting.FreeBailCardAccounterImpl
import org.monsim.impl.accounting.PropertyAccounterImpl
import java.lang.RuntimeException

class MoverImpl() : Mover {

    private val log = KotlinLogging.logger {}
    private val spacer: Spacer = SpacerImpl()
    private val gameUtil: GameUtil = GameUtilImpl()
    private val multiPlayerPayer: MultiPlayerPayer = MultiPlayerPayerImpl()
    private val freeParker: FreeParker = FreeParkerImpl()
    private val jailer: Jailer = JailerImpl()
    private val renter: Renter = RenterImpl()
    private val goCollector: GoCollector = GoCollectorImpl()
    private val taxer: Taxer = TaxerImpl()
    private val repairer: Repairer = RepairerImpl()
    private val cashAccounter: CashAccounter = CashAccounterImpl()
    private val propertyAccounter: PropertyAccounter = PropertyAccounterImpl()
    private val freeBailCardAccounter: FreeBailCardAccounter = FreeBailCardAccounterImpl()
    private val cardDrawer: CardDrawer = CardDrawerImpl()
    private val spaceClassifier: SpaceClassifier = SpaceClassifierImpl()

    override fun move(spacesToMove: Int, game: Game) {
        val moverDelegate = MoverDelegate(game)
        moverDelegate.move(spacesToMove)
    }

    inner class MoverDelegate(val game: Game){
        fun move(spacesToMove: Int) {
            val player = currentPlayer()
            val spaceMove: SpaceMove = spacer.calcSpaceMove(spacesToMove, game)
            val lastSpace = gameUtil.currentSpace(game)
            updateCurrentSpace(spaceMove.newSpace)
            val newSpace = gameUtil.currentSpace(game)
            val goPassedStr = if (spaceMove.isGoPassed) "go was passed" else ""
            log.debug {"player#${player.id} is moving ${spacesToMove} spaces to SpaceId#${newSpace.id}(${newSpace.name}) (last space was #${lastSpace.id}) $goPassedStr"}
            handleIsGoPassed(spaceMove.isGoPassed)
            applyActionOfNewSpace()
        }

        private fun applyActionOfNewSpace() {
            val player = currentPlayer()
            val space = currentSpace()
            val spaceTypeMap = spaceClassifier.classifySpaces(game)
            val spaceType = spaceTypeMap[space.id]!!.first()
            when (spaceType){
                SpaceType.FREE -> freeParker.park(game)
                SpaceType.GO -> log.debug("landed on go. (no extra action)") //already collected,
                SpaceType.JAIL -> log.debug{"landed on jail. (no extra action)"}
                SpaceType.GO_TO_JAIL -> jailer.jail(game)
                SpaceType.PROPERTY -> renter.chargeRentIfNecessary(game)
                SpaceType.TAX -> taxer.tax(gameUtil.taxForSpace(space, game)!!, game)
                SpaceType.CARD -> drawCard()
                SpaceType.UNASSIGNED -> log.debug("no action for landing on Space#${space.id}(${space.name}) since space we couldn't determine spaceType")
                else -> throw RuntimeException("Bad spaceType: $spaceType")
            }

        }

        private fun drawCard(){
            val space = currentSpace()
            val player = currentPlayer()
            val pile = gameUtil.pileForSpace(space, game)!!
            val card = cardDrawer.drawCard(pile, game)
            log.debug{"player#${player.id} drew card#${card.id}: ${card.message}"}
            applyCard(card)
        }

        private fun applyCard(card: Card) {
            val player = currentPlayer()
            val cardType = card.cardType
            when (cardType){
                CardType.CASH_FROM_BANK -> cashAccounter.applyBill(Bill(creditor = player, amount = card.cash!!), game)
                CardType.CASH_TO_BANK -> cashAccounter.applyBill(Bill(debtor = player, amount = card.cash!!), game)
                CardType.CASH_FROM_OTHER_PLAYERS -> multiPlayerPayer.cashFromOtherPlayers(card.cash!!, game)
                CardType.CASH_TO_OTHER_PLAYERS -> multiPlayerPayer.cashToOtherPlayers(card.cash!!, game)
                CardType.HOUSE_REPAIR -> repairer.chargeForHouseRepair(card.repairCostPerHouse!!, game)
                CardType.GET_OUT_OF_JAIL_FREE -> freeBailCardAccounter.transferFreeBailCards(Bill(creditor = player, amount = 1), game)
                CardType.ADVANCE_TO_SPACE -> advanceToSpace(gameUtil.spaceWithId(card.spaceId!!, game))
                CardType.ADVANCE_TO_NEAREST_PROPERTY_GROUP -> advanceToNearestPropertyGroup(gameUtil.groupForId(card.groupId!!, game))
                CardType.MOVE_SPACES -> move(card.spaceCount!!)
                CardType.GOTO_JAIL -> jailer.jail(game)
                CardType.DO_NOTHING -> {log.debug{"No action for card.  CardType is ${cardType}."}}
                else -> throw RuntimeException("Bad cardType: $cardType")
            }
        }

        private fun advanceToNearestPropertyGroup(group: Group) {
            val space: Space = spacer.spaceOfNearestPropertyInGroup(group, game)
            advanceToSpace(space)
        }

        private fun advanceToSpace(space: Space) {
            val player = currentPlayer()
            log.debug{"Player#${player.id} is advancing to spaceId#${space.id}(${space.name})"}
            val roll: Int = spacer.rollNeededToGetToSpace(space, game)
            move(roll)
        }

        private fun updateCurrentSpace(space: Space) {
            val currentPlayer = gameUtil.currentPlayer(game)
            currentPlayer.spaceId = space.id
        }

        fun handleIsGoPassed(isGoPassed: Boolean) {
            if (!isGoPassed){
                return
            }
            goCollector.collectForPassingGo(game)
        }

        fun currentPlayer(): Player{
            val currentPlayer = gameUtil.currentPlayer(game)
            return currentPlayer
        }

        fun currentSpace(): Space{
            return gameUtil.spaceWithId(currentPlayer().spaceId, game)
        }
    }


}