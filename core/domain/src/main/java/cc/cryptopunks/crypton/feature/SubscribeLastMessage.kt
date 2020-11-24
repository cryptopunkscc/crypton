package cc.cryptopunks.crypton.feature

import cc.cryptopunks.crypton.cliv2.command
import cc.cryptopunks.crypton.cliv2.config
import cc.cryptopunks.crypton.cliv2.option
import cc.cryptopunks.crypton.cliv2.optional
import cc.cryptopunks.crypton.context.Chat
import cc.cryptopunks.crypton.context.Subscribe
import cc.cryptopunks.crypton.feature
import cc.cryptopunks.crypton.inContext
import cc.cryptopunks.crypton.util.logger.log
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flattenMerge
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart

internal fun subscribeLastMessage() = feature(

    command = command(
        config("account"),
        config("chat"),
        option("cancel").optional().copy(description = "Cancel subscription", value = false),
        name = "subscribe last message",
        description = "Subscribe roster for all accounts."
    ) { (account, chat, cancel) ->
        Subscribe.LastMessage(!cancel.toBoolean()).inContext(account, chat)
    },

    handler = { out, _: Subscribe.LastMessage ->
        flowOf(
            messageRepo.list(chat.address).asFlow(),
            messageRepo.flowLatest(chat.address)
        ).flattenMerge().onEach {
            log.d { "last message $it" }
        }.map { messages ->
            Chat.Messages(
                account = address,
                list = listOf(messages)
            )
        }.onStart {
            log.d { "start LastMessageSubscription" }
        }.onCompletion {
            log.d { "finish LastMessageSubscription $it" }
        }.collect(out)
    }
)
