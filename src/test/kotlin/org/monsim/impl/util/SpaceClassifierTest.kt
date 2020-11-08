package org.monsim.impl.util

import mu.KotlinLogging
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.monsim.bean.type.SpaceType
import org.monsim.api.util.SpaceClassifier
import org.monsim.bean.domain.Game
import org.monsim.bean.type.SpaceType.PROPERTY
import org.monsim.bean.type.SpaceType.CARD
import org.monsim.bean.type.SpaceType.TAX
import org.monsim.bean.type.SpaceType.JAIL
import org.monsim.bean.type.SpaceType.FREE
import org.monsim.bean.type.SpaceType.GO_TO_JAIL
import org.monsim.bean.type.SpaceType.GO


class SpaceClassifierTest {
    private val log = KotlinLogging.logger {}

    private val spaceClassifier: SpaceClassifier = SpaceClassifierImpl()


    @Test
    fun classifySpaces() {
        val game = Game()

        val spaceTypes =
                listOf(GO, PROPERTY, CARD, PROPERTY, TAX, PROPERTY, PROPERTY, CARD, PROPERTY, PROPERTY,
                        JAIL, PROPERTY, PROPERTY, PROPERTY, PROPERTY, PROPERTY, PROPERTY, CARD, PROPERTY, PROPERTY,
                        FREE, PROPERTY, CARD, PROPERTY, PROPERTY, PROPERTY, PROPERTY, PROPERTY, PROPERTY, PROPERTY,
                        GO_TO_JAIL, PROPERTY, PROPERTY, CARD, PROPERTY, PROPERTY, CARD, PROPERTY, TAX, PROPERTY
                )
        val expected = mutableMapOf<Int, Set<SpaceType>>()
        spaceTypes.forEachIndexed { index, spaceType ->
            expected.put(1001 + index, setOf(spaceType))
        }

        val spaceTypesMap = spaceClassifier.classifySpaces(game)
        assertEquals(expected, spaceTypesMap)

    }
}