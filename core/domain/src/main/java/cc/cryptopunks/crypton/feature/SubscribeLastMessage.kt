package cc.cryptopunks.crypton.feature

import cc.cryptopunks.crypton.cliv2.command
import cc.cryptopunks.crypton.cliv2.config
import cc.cryptopunks.crypton.cliv2.option
import cc.cryptopunks.crypton.cliv2.optional
import cc.cryptopunks.crypton.context.Chat
import cc.cryptopunks.crypton.context.Subscribe
import cc.cryptopunks.crypton.context.account
import cc.cryptopunks.crypton.context.chat
import cc.cryptopunks.crypton.context.messageRepo
import cc.cryptopunks.crypton.factory.handler
import cc.cryptopunks.crypton.feature
import cc.cryptopunks.crypton.inScope
import cc.cryptopunks.crypton.logv2.d
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.distinctUntilChangedBy
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
        Subscribe.LastMessage(!cancel.toBoolean()).inScope(account, chat)
    },

    handler = handler { out, _: Subscribe.LastMessage ->
        val log = log
        val chatAddress = chat.address
        val messageRepo = messageRepo
        flowOf(
            messageRepo.list(chatAddress).asFlow(),
            messageRepo.flowLatest(chatAddress)
        ).flattenMerge().onEach {
            log.d { "last message $it" }
        }.distinctUntilChangedBy {
            it.id + it.status
        }.map { messages ->
            Chat.Messages(
                account = account.address,
                list = listOf(messages)
            )
        }.onStart {
            log.d { "start LastMessageSubscription" }
        }.onCompletion {
            log.d { "finish LastMessageSubscription $it" }
        }.collect(out)
    }
)
