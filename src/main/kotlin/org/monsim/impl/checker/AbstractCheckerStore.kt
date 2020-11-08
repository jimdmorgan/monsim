package org.monsim.impl.checker

import org.monsim.bean.domain.Game
import org.monsim.api.checker.ItemChecker

class AbstractCheckerStore {

    abstract class Abstract_Checker(private val game: Game) : ItemChecker {
        override fun aggregateError(): String? {
            return null
        }
        override fun itemError(item: Any): String? {
            return null
        }
    }

    abstract class Abstract_SingleItem_Checker(private val game: Game) : ItemChecker {

        override fun aggregateError(): String? {
            return null
        }
    }


}

