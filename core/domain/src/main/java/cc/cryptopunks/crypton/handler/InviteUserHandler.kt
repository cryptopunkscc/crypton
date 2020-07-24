package cc.cryptopunks.crypton.handler

import cc.cryptopunks.crypton.context.Chat
import cc.cryptopunks.crypton.handle

internal fun handleInvite() = handle { _, (users): Chat.Service.Invite ->
    require(chat.isConference) { "Cannot invite to direct chat" }
    inviteToConference(chat.address, users)
}
