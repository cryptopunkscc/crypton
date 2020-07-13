package cc.cryptopunks.crypton.handler

import cc.cryptopunks.crypton.context.Chat
import cc.cryptopunks.crypton.context.SessionScope
import cc.cryptopunks.crypton.context.handle
import cc.cryptopunks.crypton.context.insertChat

internal fun SessionScope.handleConferenceInvitations() =
    handle<Chat.Net.ConferenceInvitation> {
        log.d(it)
        if (!chatRepo.contains(address)) {
            insertChat(
                Chat(
                    address = address,
                    account = this@handleConferenceInvitations.address
                )
            )
        }
    }
