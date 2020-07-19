package cc.cryptopunks.crypton.handler

import cc.cryptopunks.crypton.context.Presence
import cc.cryptopunks.crypton.context.Resource
import cc.cryptopunks.crypton.context.Roster
import cc.cryptopunks.crypton.context.SessionScope
import cc.cryptopunks.crypton.handle

internal fun SessionScope.handleJoin() =
    handle<Roster.Service.Join> {
        when (chatRepo.get(chat).isConference) {
            true -> joinConference(chat, account.local)
            false -> {
                sendPresence(
                    Presence(
                        resource = Resource(chat),
                        status = Presence.Status.Subscribed
                    )
                )
                sendPresence(
                    Presence(
                        resource = Resource(chat),
                        status = Presence.Status.Subscribe
                    )
                )
            }
        }
    }
