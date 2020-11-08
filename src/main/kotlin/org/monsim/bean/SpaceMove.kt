package org.monsim.bean

import org.monsim.bean.domain.Space


data class SpaceMove(
        val newSpace: Space,
        val isGoPassed: Boolean
)

