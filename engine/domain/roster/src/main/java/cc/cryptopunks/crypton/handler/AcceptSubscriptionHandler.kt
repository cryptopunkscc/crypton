package cc.cryptopunks.crypton.handler

import cc.cryptopunks.crypton.context.AppScope
import cc.cryptopunks.crypton.context.Presence
import cc.cryptopunks.crypton.context.Resource
import cc.cryptopunks.crypton.context.Roster
import cc.cryptopunks.crypton.context.handle

internal fun AppScope.handleSubscriptionAccept() =
    handle<Roster.Service.AcceptSubscription> {
        sessionStore.get()[account]?.run {
            if (chatRepo.get(accepted).isDirect) {
                sendPresence(
                    Presence(
                        resource = Resource(accepted),
                        status = Presence.Status.Subscribed
                    )
                )
                sendPresence(
                    Presence(
                        resource = Resource(accepted),
                        status = Presence.Status.Subscribe
                    )
                )
            } else {
                joinMuc(accepted, account.local)
            }
        } ?: throw IllegalArgumentException("Invalid account $account")
    }
