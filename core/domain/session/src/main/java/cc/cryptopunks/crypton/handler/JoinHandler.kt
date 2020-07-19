package cc.cryptopunks.crypton.handler

import cc.cryptopunks.crypton.context.Address
import cc.cryptopunks.crypton.context.Presence
import cc.cryptopunks.crypton.context.Resource
import cc.cryptopunks.crypton.context.Roster
import cc.cryptopunks.crypton.context.SessionScope
import cc.cryptopunks.crypton.handle
import java.lang.System.currentTimeMillis

internal fun SessionScope.handleJoin() =
    handle<Roster.Service.Join> {
        when (chatRepo.get(chat).isConference) {
            true -> joinConference(
                address = chat,
                nickname = account.local,
                historySince = historySince(chat)
            )
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

private suspend fun SessionScope.historySince(chat: Address): Int = messageRepo.latest(chat)?.run {
    (currentTimeMillis() - timestamp).toInt()
} ?: 0
