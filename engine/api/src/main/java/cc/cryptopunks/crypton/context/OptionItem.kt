package cc.cryptopunks.crypton.context

import kotlinx.coroutines.flow.Flow

object OptionItem {

    interface Select {
        suspend operator fun invoke(idRes: Int)
    }

    interface Output : Flow<Int>

    interface Core {
        val selectOptionItem: Select
        val optionItemSelections: Output
    }
}