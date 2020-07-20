import cc.cryptopunks.crypton.context.Chat
import cc.cryptopunks.crypton.context.ChatScope
import cc.cryptopunks.crypton.handle

internal fun ChatScope.handleConfigureChat() =
    handle<Chat.Service.Configure> { out ->
        configureConference(this@handleConfigureChat.chat.address)
    }
