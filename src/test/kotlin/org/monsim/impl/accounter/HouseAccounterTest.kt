package org.monsim.impl.accounter

import mu.KotlinLogging
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.monsim.bean.domain.Game
import org.monsim.bean.domain.Group
import org.monsim.bean.domain.Property
import org.monsim.bean.type.GroupType
import org.monsim.impl.accounting.HouseAccounterImpl

class HouseAccounterTest {
    private val log = KotlinLogging.logger {}
    private val houseTransferer = HouseAccounterImpl()

    @Test
    fun isPropertyAbleToAddHouse() {
        assertNull(houseTransferer.errorIfHouseAddedToProperty(Property(id = 21, groupId = 9, houses = 2),
                Game(
                        properties = listOf(
                                Property(id = 22, groupId = 9, houses = null, ownerId = 1),
                                Property(id = 23, groupId = 9, houses = 4, ownerId = 1),
                        ),
                        groups = listOf(
                                Group(id = 9, groupType = GroupType.DEVELOPABLE)
                        )
                )
        ))
        assertNotNull(houseTransferer.errorIfHouseAddedToProperty(Property(id = 21, groupId = 9, houses = 2),
                Game(
                        properties = listOf(
                                Property(id = 22, groupId = 9, houses = 4),
                                Property(id = 23, groupId = 9, houses = 1),
                        ),
                        groups = listOf(
                                Group(id = 9, groupType = GroupType.DEVELOPABLE)
                        )
                )
        ))
        assertNotNull(houseTransferer.errorIfHouseAddedToProperty(Property(id = 21, groupId = 9, houses = 5),
                Game(
                        properties = listOf(
                                Property(id = 22, groupId = 9, houses = 5),
                                Property(id = 23, groupId = 9, houses = 5),
                        ),
                        groups = listOf(
                                Group(id = 9, groupType = GroupType.DEVELOPABLE)
                        )
                )
        ))
    }

    @Test
    fun isPropertyAbleToRemoveHouse() {
        assertNull(houseTransferer.errorIfHouseRemovedFromProperty(Property(id = 21, groupId = 9, houses = 2),
                Game(
                        properties = listOf(
                                Property(id = 22, groupId = 9, houses = null, ownerId = 1),
                                Property(id = 23, groupId = 9, houses = 1, ownerId = 1),
                        ),
                        groups = listOf(
                                Group(id = 9, groupType = GroupType.DEVELOPABLE)
                        )
                )
        ))
        assertNotNull(houseTransferer.errorIfHouseRemovedFromProperty(Property(id = 21, groupId = 9, houses = 2),
                Game(
                        properties = listOf(
                                Property(id = 23, groupId = 9, houses = 3),
                                Property(id = 23, groupId = 9, houses = 2),
                        ),
                        groups = listOf(
                                Group(id = 9, groupType = GroupType.DEVELOPABLE)
                        )
                )
        ))
        assertNotNull(houseTransferer.errorIfHouseRemovedFromProperty(Property(id = 21, groupId = 9, houses = 0),
                Game(
                        properties = listOf(
                                Property(id = 23, groupId = 9, houses = 0),
                        ),
                        groups = listOf(
                                Group(id = 9, groupType = GroupType.DEVELOPABLE)
                        )
                )
        ))
    }


}