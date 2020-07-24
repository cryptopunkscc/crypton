import cc.cryptopunks.crypton.context.Chat
import cc.cryptopunks.crypton.handle

internal fun handleConfigureChat() = handle { _, _: Chat.Service.Configure ->
    configureConference(chat.address)
}
