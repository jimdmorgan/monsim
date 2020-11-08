package org.monsim.bean.type

/** The different types of cards in the Chance or Community chest piles */
enum class CardType {
    CASH_FROM_BANK,  //the player gets cash from the bank
    CASH_FROM_OTHER_PLAYERS, //the player gets x amount of cash from every other player
    CASH_TO_BANK, //the player must pay the bank
    CASH_TO_OTHER_PLAYERS, //the player must pay every other player x amount of cash
    HOUSE_REPAIR, //the player must pay x dollars for each house he or she owns
    ADVANCE_TO_SPACE, //the player will advance to space x, and collect $200 from go if passed (e.g. advance to Boardwalk)
    ADVANCE_TO_NEAREST_PROPERTY_GROUP, //the player will advance to the nearest property group (e.g. advance to nearest railroad)
    MOVE_SPACES, //the player will be moved forward or backward x spaces.  (e.g. go back 3 spaces)
    GOTO_JAIL, //the player will be sent to jail
    GET_OUT_OF_JAIL_FREE, //the player will be given a "Get Out of Jail Free Card"
    DO_NOTHING //no action will happen
}