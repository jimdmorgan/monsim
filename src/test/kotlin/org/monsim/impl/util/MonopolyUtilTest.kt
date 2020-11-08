package org.monsim.impl.util

import mu.KotlinLogging
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.monsim.bean.domain.Game
import org.monsim.bean.domain.Group
import org.monsim.bean.domain.Property
import org.monsim.bean.type.GroupType

class MonopolyUtilTest {
    private val log = KotlinLogging.logger {}
    private val monopolyUtil = MonopolyUtilImpl()

    @Test
    fun isGroupMonopoly() {
        assertTrue(monopolyUtil.isGroupMonopoly(Group(id = 9),
                Game(properties = listOf(
                        Property(id = 1, groupId = 9, ownerId = 1000),
                        Property(id = 2, groupId = 9, ownerId = 1000),
                        Property(id = 3, groupId = 8, ownerId = null),
                )
                )
        ))
        assertFalse(monopolyUtil.isGroupMonopoly(Group(id = 9),
                Game(properties = listOf(
                        Property(id = 1, groupId = 9, ownerId = 1000),
                        Property(id = 2, groupId = 9, ownerId = null),
                )
                )
        ))
    }

    @Test
    fun isGroupWithHouses() {
        assertTrue(monopolyUtil.isGroupWithHouses(Group(id = 9, groupType = GroupType.DEVELOPABLE),
                Game(properties = listOf(
                        Property(id = 1, groupId = 9, houses = null),
                        Property(id = 2, groupId = 9, houses = 1),
                )
                )
        ))
        assertFalse(monopolyUtil.isGroupWithHouses(Group(id = 9, groupType = GroupType.DEVELOPABLE),
                Game(properties = listOf(
                        Property(id = 1, groupId = 9, houses = 0),
                        Property(id = 2, groupId = 8, houses = 1),
                )
                )
        ))
    }



}