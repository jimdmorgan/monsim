package org.monsim.api.accounting

import org.monsim.bean.Bill
import org.monsim.bean.domain.Game

interface CashAccounter {

    /**
     * Applies a bill (which contains a debitor, a creditor, and an amount).
     * All the debitor's available cash is immediately taken and given to the creditor.
     * So if the bill is for $200 and the debtor's available cash is $50, and the creditor's current cash is $1000,
     * then the creditor's new cash is set to $1050, the debtor's cash is set to $-150.
     *
     * As the debtor acquires more cash (say by mortgaging property), the cash is immediately turned over to the creditor.
     * So let's say the debtor gets $40 by mortgaging a property, then the debtor's new cash is -$110, and then creditor's cash
     * becomes $1090.
     *
     * The bank never goes into debt, so if the debtor is the bank, then the bill is always paid in full and the creditor
     * receives the full amount.
     */
    fun applyBill(bill: Bill, game: Game)
}