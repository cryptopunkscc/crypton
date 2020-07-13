package cc.cryptopunks.crypton.handler

import cc.cryptopunks.crypton.context.Address
import cc.cryptopunks.crypton.context.Chat
import cc.cryptopunks.crypton.context.ChatScope
import cc.cryptopunks.crypton.context.SessionScope
import cc.cryptopunks.crypton.context.handle
import cc.cryptopunks.crypton.context.isConference

internal fun SessionScope.handleInvite() =
    handle<Chat.Service.Invite> {
        require(chat.isConference) { "Cannot invite to direct chat" }
        inviteToConference(chat, users)
    }

internal fun ChatScope.handleInvite() =
    handle<Chat.Service.Invite> {
        val chat = chat.takeIf { it != Address.Empty } ?: this@handleInvite.chat.address
        require(chat.isConference) { "Cannot invite to direct chat" }
        inviteToConference(chat, users)
    }
