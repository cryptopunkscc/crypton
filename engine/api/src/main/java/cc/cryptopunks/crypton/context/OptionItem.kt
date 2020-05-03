package cc.cryptopunks.crypton.context

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow

object OptionItem {

    interface Select {
        suspend operator fun invoke(idRes: Int)
    }

    interface Output : Flow<Int>

    class Selections : Channel<OptionItem> by Channel()

    interface Core {
        val selectOptionItem: Select
        val optionItemSelections: Output
    }
}
