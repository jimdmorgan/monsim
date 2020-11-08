package org.monsim.api.checker

interface ItemChecker {

    fun itemsToCheck(): List<Any>
    fun itemError(item: Any): String?
    fun aggregateError(): String?

}