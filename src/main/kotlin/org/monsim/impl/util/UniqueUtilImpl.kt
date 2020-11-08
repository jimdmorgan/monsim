package org.monsim.impl.util

import mu.KotlinLogging
import org.monsim.bean.domain.Space
import org.monsim.api.util.UniqueUtil

class UniqueUtilImpl : UniqueUtil {

    private val log = KotlinLogging.logger {}

    override fun singleError(message: String): Set<String> {
        val errors = initErrors()
        errors.add(message)
        return errors;
    }

    override fun noErrors(): Set<String> {
        return emptySet()
    }

    override fun repeatedInts(intsList: List<Int>): Set<Int> {
        val set = intsList.toMutableSet()
        val nonUnique = mutableSetOf<Int>()
        intsList.forEach {
            if (set.contains(it)) {
                set.remove(it)
            } else {
                nonUnique.add(it)
            }
        }
        return nonUnique
    }

    override fun repeatedStrings(stringsList: List<String>): Set<String> {
        val set = stringsList.toMutableSet()
        val nonUnique = mutableSetOf<String>()
        stringsList.forEach {
            if (set.contains(it)) {
                set.remove(it)
            } else {
                nonUnique.add(it)
            }
        }
        return nonUnique
    }

    override fun spaceIds(spaces: List<Space>): Set<Int> {
        val spaceIds = spaces.map { it.id }.toSet()
        return spaceIds
    }

    override fun initErrors(): MutableSet<String> {
        val errors = mutableSetOf<String>()
        return errors;
    }


    override fun <T> itemsReferToValidSpace(
            items: List<T>,
            idGetter: (item: T) -> Int,
            spaceIdGetter: (item: T) -> Int,
            spaces: List<Space>,
            itemDescription: String)
            : Set<String> {
        val errors = initErrors()
        val spaceIds = spaceIds(spaces)
        items.forEach {
            val item = it
            val spaceId = spaceIdGetter(item)
            if (!spaceIds.contains(spaceId)) {
                val id = idGetter(item)
                val error = "${itemDescription}#${id} refers to a spaceId that doesn't exist: ${spaceId}"
                errors.add(error)
            }
        }
        return errors
    }

    override fun <T> itemsHaveUniqueIds(
            items: List<T>,
            idGetter: (item: T) -> Int,
            itemDescription: String
    ) : Set<String> {
        val errors = initErrors()
        val ids = items.map(idGetter).toList()
        val nonUniqueIds = repeatedInts(ids)
        nonUniqueIds.forEach {
            errors.add("More than 1 ${itemDescription} uses the same id of #$it")
        }
        return errors
    }

    override fun <T> itemsHaveUniqueNames(
            items: List<T>,
            nameGetter: (item: T) -> String,
            itemDescription: String,
            fieldDescription: String)
            : Set<String> {
        val errors = initErrors()
        val names = items.map(nameGetter).toList()
        val nonUniqueNames = repeatedStrings(names)
        nonUniqueNames.forEach {
            errors.add("More than 1 ${itemDescription} uses the same ${fieldDescription} of '$it'")
        }
        return errors
    }
}
