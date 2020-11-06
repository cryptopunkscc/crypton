package cc.cryptopunks.crypton.handler

import cc.cryptopunks.crypton.context.Chat
import cc.cryptopunks.crypton.context.Subscribe
import cc.cryptopunks.crypton.handle
import cc.cryptopunks.crypton.util.logger.log
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flattenMerge
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart

internal fun handleLastMessageSubscription() =
    handle { out, _: Subscribe.LastMessage ->
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
