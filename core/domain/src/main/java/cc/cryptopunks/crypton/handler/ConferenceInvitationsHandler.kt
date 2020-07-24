package cc.cryptopunks.crypton.handler

import cc.cryptopunks.crypton.context.Chat
import cc.cryptopunks.crypton.context.insertChat
import cc.cryptopunks.crypton.handle

internal fun handleConferenceInvitations() = handle { _, arg: Chat.Net.ConferenceInvitation ->
    log.d("handle $arg")
    if (!chatRepo.contains(arg.address)) {
        insertChat(
            Chat(
                address = arg.address,
                account = address
            )
        )
    }
}
