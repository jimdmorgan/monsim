package org.monsim.bean.type

enum class GroupType {

    /** Properties with this groupType can have houses put upon them (assuming all in the group are owned) e.g. Park Place & Boardwalk */
    DEVELOPABLE,

    /** Properties with this groupType have a fixed rent, based upon the number of properties the player owns in the group.  e.g. the Railroads */
    FIXED,

    /** Properties in this groupType have a rent that is based up number of properties owned in the group and dice sum rolled  e.g. Electric Company */
    DICE
}