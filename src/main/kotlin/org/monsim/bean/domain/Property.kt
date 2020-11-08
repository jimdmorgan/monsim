package org.monsim.bean.domain

import org.monsim.bean.Constants

data class Property (
        val id: Int,
        val groupId: Int = 0, //the group this property refers to (e.g. the 'Railroads' group)
        val spaceId: Int = 0, //the spaceId this property refers to
        val schedule: Int = 1, //properties in the same group often have different "schedules".  See Group for details
        var ownerId: Int? = Constants.BANK_ID, //null means property has no owner; otherwise the playerId of the owner
        var houses: Int? = 0, //null if not developable; otherwise the number of houses
        var mortgaged: Boolean = false, //true if the property is mortgaged (the owner can't collect rent)
        var incomeGenerated: Int = 0 //the total amount of income this property has produced this game

)
