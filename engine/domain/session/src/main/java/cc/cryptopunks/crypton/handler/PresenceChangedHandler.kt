package cc.cryptopunks.crypton.handler

import cc.cryptopunks.crypton.context.Chat
import cc.cryptopunks.crypton.context.Presence
import cc.cryptopunks.crypton.context.Roster
import cc.cryptopunks.crypton.context.Session
import cc.cryptopunks.crypton.context.createChat
import cc.cryptopunks.crypton.context.handle
import cc.cryptopunks.crypton.interactor.StorePresenceInteractor
import cc.cryptopunks.crypton.interactor.flushQueuedMessages
import kotlinx.coroutines.launch

internal fun Session.handlePresenceChange(
    storePresence: StorePresenceInteractor
) = handle<Roster.Net.PresenceChanged> {
    scope.launch {

        storePresence(presence)

        when (presence.status) {

            Presence.Status.Subscribe ->
                if (iAmSubscribed(presence.resource.address)) {
                    log.d("Auto accepting feedback subscription from ${presence.resource}")
                    sendPresence(
                        Presence(
                            resource = presence.resource,
                            status = Presence.Status.Subscribed
                        )
                    )
                } else {
                    log.d("Received subscription request from ${presence.resource}")
                    createChat(
                        Chat.Service.CreateChatData(
                            title = presence.resource.address.toString(),
                            users = listOf(presence.resource.address)
                        )
                    )
                }

            Presence.Status.Subscribed -> {
                log.d("${presence.resource} accepted subscription")
                flushQueuedMessages(presence.resource.address)
            }

            else -> Unit
        }
    }
}
