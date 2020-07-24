package cc.cryptopunks.crypton.handler

import cc.cryptopunks.crypton.context.Presence
import cc.cryptopunks.crypton.context.Resource
import cc.cryptopunks.crypton.context.Roster
import cc.cryptopunks.crypton.handle

internal fun handleSubscriptionAccept() = handle { _, _: Roster.Service.Join ->
    when (chatRepo.get(chat.address).isConference) {
        true -> joinConference(chat.address, address.local)
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
