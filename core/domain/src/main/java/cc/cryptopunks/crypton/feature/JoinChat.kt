package cc.cryptopunks.crypton.feature

import cc.cryptopunks.crypton.cliv2.command
import cc.cryptopunks.crypton.cliv2.config
import cc.cryptopunks.crypton.context.Address
import cc.cryptopunks.crypton.context.Chat
import cc.cryptopunks.crypton.context.Exec
import cc.cryptopunks.crypton.context.Presence
import cc.cryptopunks.crypton.context.Resource
import cc.cryptopunks.crypton.context.SessionScope
import cc.cryptopunks.crypton.context.SessionScopeTag
import cc.cryptopunks.crypton.context.account
import cc.cryptopunks.crypton.context.chat
import cc.cryptopunks.crypton.context.chatNet
import cc.cryptopunks.crypton.context.chatRepo
import cc.cryptopunks.crypton.context.inScope
import cc.cryptopunks.crypton.context.messageRepo
import cc.cryptopunks.crypton.context.rosterNet
import cc.cryptopunks.crypton.create.emitter
import cc.cryptopunks.crypton.create.handler
import cc.cryptopunks.crypton.create.inScope
import cc.cryptopunks.crypton.feature
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
        Exec.JoinChat.inScope(account, chat)
    },

    emitter = emitter(SessionScopeTag) {
        val chatRepo = chatRepo
        val chatNet = chatNet
        accountAuthenticatedFlow().take(1).flatMapMerge {
            chatRepo.flowList()
        }.bufferedThrottle(300).flatMapConcat { list ->
            list.asSequence()
                .flatten()
                .filter(Chat::isConference)
                .map(Chat::address)
                .toSet()
                .minus(chatNet.listJoinedRooms())
                .asFlow()
        }.map { chat ->
            Exec.JoinChat.inScope(account.address, chat)
        }
    },

    handler = handler { _, _: Exec.JoinChat ->
        val chat = chat
        when (chat.isConference) {
            true -> chatNet.joinConference(
                address = chat.address,
                nickname = account.address.local,
                historySince = historySince(chat.address)
            )
            false -> rosterNet.run {
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
