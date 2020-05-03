package cc.cryptopunks.crypton.selector

import cc.cryptopunks.crypton.context.Chat
import cc.cryptopunks.crypton.context.Clip
import cc.cryptopunks.crypton.util.pop

internal class PopClipboardMessageSelector(
    private val store: Clip.Board.Store
) {
    suspend operator fun invoke() = store.pop()?.run {
        Chat.Service.MessageText(data)
    }
}
