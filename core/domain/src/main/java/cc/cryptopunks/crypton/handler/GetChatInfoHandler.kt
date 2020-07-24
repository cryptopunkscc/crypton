import cc.cryptopunks.crypton.context.Chat
import cc.cryptopunks.crypton.handle

internal fun handleGetChatInfo() = handle { out, _: Chat.Service.GetInfo ->
    out(getChatInfo(chat.address))
}

