package cc.cryptopunks.crypton.handler

import cc.cryptopunks.crypton.context.Chat
import cc.cryptopunks.crypton.context.Presence
import cc.cryptopunks.crypton.context.Roster
import cc.cryptopunks.crypton.context.SessionScope
import cc.cryptopunks.crypton.context.asChat
import cc.cryptopunks.crypton.context.createChat
import cc.cryptopunks.crypton.context.handle
import cc.cryptopunks.crypton.interactor.storePresence

internal fun SessionScope.handlePresenceChanged() =
    handle<Roster.Net.PresenceChanged> {
        log.d("handle $this")
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
                        Chat.Service.CreateChat(
                            account = address,
                            chat = presence.resource.address,
                            users = listOf(presence.resource.address)
                        ).asChat()
                    )
                }

            else -> Unit
        }
    }
