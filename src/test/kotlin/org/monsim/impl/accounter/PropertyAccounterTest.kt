package org.monsim.impl.accounter

import mu.KotlinLogging
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.monsim.bean.domain.Game
import org.monsim.bean.domain.Group
import org.monsim.bean.domain.Player
import org.monsim.bean.domain.Property
import org.monsim.impl.accounting.PropertyAccounterImpl

class PropertyAccounterTest {
    private val log = KotlinLogging.logger {}
    private val propertyTransferer = PropertyAccounterImpl()

    @Test
    fun transferProperty() {
        val player = Player(id = 1)
        val property = Property(id = 101, groupId = 9, ownerId = null)
        val game = Game(
                groups = listOf(Group(id = 9))
        )
        propertyTransferer.transferProperty(property, player, game)
        assertEquals(1, property.ownerId)
    }



}