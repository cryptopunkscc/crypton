package cc.cryptopunks.crypton.feature

import cc.cryptopunks.crypton.cliv2.command
import cc.cryptopunks.crypton.cliv2.config
import cc.cryptopunks.crypton.context.Address
import cc.cryptopunks.crypton.context.Chat
import cc.cryptopunks.crypton.context.Exec
import cc.cryptopunks.crypton.context.Presence
import cc.cryptopunks.crypton.context.Resource
import cc.cryptopunks.crypton.context.SessionScope
import cc.cryptopunks.crypton.context.inContext
import cc.cryptopunks.crypton.emitter
import cc.cryptopunks.crypton.feature
import cc.cryptopunks.crypton.inContext
import cc.cryptopunks.crypton.selector.accountAuthenticatedFlow
import cc.cryptopunks.crypton.util.ext.bufferedThrottle
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flatMapMerge
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.take

internal fun joinChat() = feature(

    command = command(
        config("account"),
        config("chat"),
        name = "join",
        description = "Accept buddy invitation or join to conference."
    ) { (account, chat) ->
        Exec.JoinChat.inContext(account, chat)
    },

    emitter = emitter<SessionScope> {
        accountAuthenticatedFlow().take(1).flatMapMerge {
            chatRepo.flowList()
        }.bufferedThrottle(300).flatMapConcat { list ->
            list.asSequence()
                .flatten()
                .filter(Chat::isConference)
                .map(Chat::address)
                .toSet()
                .minus(listJoinedRooms())
                .asFlow()
        }.map { chat ->
            Exec.JoinChat.inContext(address, chat)
        }
    },

    handler = { _, _: Exec.JoinChat ->
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
)

private suspend fun SessionScope.historySince(chat: Address): Long =
    messageRepo.latest(chat)?.run { timestamp + 1 } ?: 0
