package org.monsim.bean.domain


data class Pile(
        val id: Int,
        val name: String = "", //the name of the pile, such as "chance" or "community chest"
        val spaceIds: Set<Int> = setOf()
)

