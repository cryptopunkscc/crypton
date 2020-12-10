package cc.cryptopunks.crypton.feature

import cc.cryptopunks.crypton.cliv2.command
import cc.cryptopunks.crypton.cliv2.config
import cc.cryptopunks.crypton.cliv2.text
import cc.cryptopunks.crypton.context.Exec
import cc.cryptopunks.crypton.context.address
import cc.cryptopunks.crypton.feature
import cc.cryptopunks.crypton.inContext

internal fun inviteToConference() = feature(

    command = command(
        config("account"),
        config("chat"),
        text().copy(name = "local1@domain, local2@domain"),
        name = "invite",
        description = "Invite users to conference"
    ) { (account, chat, users) ->
        Exec.Invite(
            users.split(" ", ",").map { address(it) }.toSet()
        ).inContext(account, chat)
    },

    handler = { _, (users): Exec.Invite ->
        require(chat.isConference) { "Cannot invite to direct chat" }
        chatNet.inviteToConference(chat.address, users)
    }
)
