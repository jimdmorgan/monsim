package org.monsim.api.util

import org.monsim.bean.domain.Space


interface UniqueUtil {
    fun singleError(message: String): Set<String>
    fun noErrors(): Set<String>

    fun repeatedInts(intsList: List<Int>): Set<Int>
    fun repeatedStrings(stringsList: List<String>): Set<String>
    fun spaceIds(space: List<Space>): Set<Int>
    fun initErrors(): MutableSet<String>

    fun <T> itemsHaveUniqueIds(
            items: List<T>,
            idGetter: (item: T) -> Int,
            itemDescription: String
    ): Set<String>

    fun <T> itemsHaveUniqueNames(
            items: List<T>,
            nameGetter: (item: T) -> String,
            itemDescription: String,
            fieldDescription: String
    ): Set<String>

    fun <T> itemsReferToValidSpace(
            items: List<T>,
            idGetter: (item: T) -> Int,
            spaceIdGetter: (item: T) -> Int,
            spaces: List<Space>,
            itemDescription: String
    ): Set<String>

}