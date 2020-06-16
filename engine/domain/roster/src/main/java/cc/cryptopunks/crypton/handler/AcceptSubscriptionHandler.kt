package cc.cryptopunks.crypton.handler

import cc.cryptopunks.crypton.context.Presence
import cc.cryptopunks.crypton.context.Resource
import cc.cryptopunks.crypton.context.Roster
import cc.cryptopunks.crypton.context.Session
import cc.cryptopunks.crypton.context.handle
import kotlinx.coroutines.launch

internal fun handleSubscriptionAccept(
    sessionStore: Session.Store
) = handle<Roster.Service.AcceptSubscription> {
    sessionStore.get()[account]?.run {
        scope.launch {
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
        }
    } ?: throw IllegalArgumentException("Invalid account $account")
}
