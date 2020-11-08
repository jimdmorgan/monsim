package org.monsim.bean

import org.monsim.bean.domain.Player


data class Bill(
        val debtor: Player? = Constants.BANK,
        val creditor: Player? = Constants.BANK,
        val amount: Int  //must be non-negative

)

