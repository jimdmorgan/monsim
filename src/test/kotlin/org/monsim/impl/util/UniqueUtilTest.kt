package org.monsim.impl.util

import mu.KotlinLogging
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.monsim.bean.domain.Property
import org.monsim.bean.domain.Space

class UniqueUtilTest {
    private val log = KotlinLogging.logger {}

    val uniqueUtil = UniqueUtilImpl()


    @Test
    fun repeatedInts() {
        assertEquals(setOf<Int>(),
                uniqueUtil.repeatedInts(listOf(5, 6, 7))
        )
        assertEquals(setOf<Int>(6, 7),
                uniqueUtil.repeatedInts(listOf(5, 6, 6, 7, 7, 7, 8))
        )
    }

    @Test
    fun repeatedStrings() {
        assertEquals(setOf<String>(),
                uniqueUtil.repeatedStrings(listOf("a", "b", "c"))
        )
        assertEquals(setOf<String>("b", "c"),
                uniqueUtil.repeatedStrings(listOf("a", "b", "b", "c", "c", "c", "d"))
        )
    }

    @Test
    fun initErrors() {
        assertEquals(setOf<String>(),
                uniqueUtil.initErrors())

        val errors = uniqueUtil.initErrors()
        errors.add("test")
        assertEquals(setOf("test"), errors)
    }


    @Test
    fun spaceIds() {
        assertEquals(setOf(5, 6),
                uniqueUtil.spaceIds(listOf(
                        Space(id = 5),
                        Space(id = 6)
                ))
        )
    }

    @Test
    fun itemsReferToValidSpace() {
        assertEquals(setOf<String>(),
                uniqueUtil.itemsReferToValidSpace(
                        listOf(
                                Property(id = 1, spaceId = 5, groupId = 0),
                                Property(id = 2, spaceId = 6, groupId = 0)
                        ),
                        { it.id },
                        { it.spaceId },
                        listOf(
                                Space(id = 5),
                                Space(id = 6)
                        ),
                        "Property"
                )
        )
        assertEquals(setOf<String>("Property#2 refers to a spaceId that doesn't exist: 7"),
                uniqueUtil.itemsReferToValidSpace(
                        listOf(
                                Property(id = 1, spaceId = 5, groupId = 0),
                                Property(id = 2, spaceId = 7, groupId = 0)
                        ),
                        { it.id },
                        { it.spaceId },
                        listOf(
                                Space(id = 5),
                                Space(id = 6)
                        ),
                        "Property"
                )
        )
    }


    @Test
    fun itemsHaveUniqueIds() {
        assertEquals(setOf<String>(),
                uniqueUtil.itemsHaveUniqueIds(
                        listOf(
                                Property(id = 1, spaceId = 0, groupId = 0),
                                Property(id = 2, spaceId = 0, groupId = 0)
                        ),
                        { it.id },
                        "Property"
                )
        )
        assertEquals(setOf<String>("More than 1 Property uses the same id of #5"),
                uniqueUtil.itemsHaveUniqueIds(
                        listOf(
                                Property(id = 5, spaceId = 0, groupId = 0),
                                Property(id = 5, spaceId = 0, groupId = 0)
                        ),
                        { it.id },
                        "Property"
                )
        )

    }

    @Test
    fun itemsHaveUniqueNames() {
        assertEquals(setOf<String>(),
                uniqueUtil.itemsHaveUniqueNames(
                        listOf(
                                Space(id = 0, name = "a"),
                                Space(id = 0, name = "b")
                        ),
                        { it.name},
                        "Space",
                        "name"
                )
        )
        assertEquals(setOf<String>("More than 1 Space uses the same name of 'a'"),
                uniqueUtil.itemsHaveUniqueNames(
                        listOf(
                                Space(id = 0, name = "a"),
                                Space(id = 1, name = "a")
                        ),
                        { it.name},
                        "Space",
                        "name"
                )
        )
    }

}