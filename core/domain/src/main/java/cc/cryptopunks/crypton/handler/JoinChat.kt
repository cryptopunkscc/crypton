package cc.cryptopunks.crypton.handler

import cc.cryptopunks.crypton.context.Address
import cc.cryptopunks.crypton.context.Exec
import cc.cryptopunks.crypton.context.Presence
import cc.cryptopunks.crypton.context.Resource
import cc.cryptopunks.crypton.context.SessionScope
import cc.cryptopunks.crypton.handle
import java.lang.System.currentTimeMillis

internal fun handleJoinChat() =
    handle { _, _: Exec.JoinChat ->
        when (chat.isConference) {
            true -> joinConference(
                address = chat.address,
                nickname = address.local,
                historySince = historySince(chat.address)
            )
            false -> {
                sendPresence(
                    Presence(
                        resource = Resource(chat.address),
                        status = Presence.Status.Subscribed
                    )
                )
                sendPresence(
                    Presence(
                        resource = Resource(chat.address),
                        status = Presence.Status.Subscribe
                    )
                )
            }
        }
    }

private suspend fun SessionScope.historySince(chat: Address): Int = messageRepo.latest(chat)?.run {
    (currentTimeMillis() - timestamp).toInt()
} ?: 0
