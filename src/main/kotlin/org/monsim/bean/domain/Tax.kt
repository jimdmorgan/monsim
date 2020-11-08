package org.monsim.bean.domain


data class Tax(
        val spaceId: Int = 0, //the space this tax applies to
        val amount: Int = 0  //the amount of the tax.  For example, the Luxury tax is $75 (or $100 in some editions)
)
