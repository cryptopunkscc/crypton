package cc.cryptopunks.crypton.handler

import cc.cryptopunks.crypton.context.Exec
import cc.cryptopunks.crypton.handle

internal fun handleInvite() = handle { _, (users): Exec.Invite ->
    require(chat.isConference) { "Cannot invite to direct chat" }
    inviteToConference(chat.address, users)
}
