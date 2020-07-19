import cc.cryptopunks.crypton.context.AppScope
import cc.cryptopunks.crypton.context.Chat
import cc.cryptopunks.crypton.context.ChatScope
import cc.cryptopunks.crypton.handle

internal fun ChatScope.handleGetChatInfo() =
    handle<Chat.Service.GetInfo> { out ->
        out(getChatInfo(this@handleGetChatInfo.chat.address))
    }

internal fun AppScope.handleGetChatInfo() =
    handle<Chat.Service.GetInfo> { out ->
        sessions[chat!!]!!.run {
            out(getChatInfo(address))
        }
    }
