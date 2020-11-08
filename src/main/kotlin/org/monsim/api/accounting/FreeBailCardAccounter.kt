package org.monsim.api.accounting

import org.monsim.bean.Bill
import org.monsim.bean.domain.Game

interface FreeBailCardAccounter {

    fun transferFreeBailCards(bill: Bill, game: Game)
}