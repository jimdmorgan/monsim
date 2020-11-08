package org.monsim.bean.type

enum class SpaceType {

    FREE, //the free parking space
    PROPERTY, //any of the property spaces (like BoardWalk)
    TAX, //the luxury tax or income tax space
    GO, //as it sounds
    CARD, //the Community Chest and Chance Spaces
    JAIL, //the jail space (both jail and 'just visiting')
    GO_TO_JAIL, //the space with a police officer saying "go to jail"
    UNASSIGNED //the spaceType couldn't be determined
}