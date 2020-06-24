package cc.cryptopunks.crypton.handler

import cc.cryptopunks.crypton.context.Chat
import cc.cryptopunks.crypton.context.SessionScope
import cc.cryptopunks.crypton.context.handle
import cc.cryptopunks.crypton.util.ext.bufferedThrottle
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flattenMerge
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

internal fun SessionScope.handleLastMessageSubscription() =
    handle<Chat.Service.SubscribeLastMessage> { out ->
        launch {
            flowOf(
                messageRepo.list().asFlow(),
                messageRepo.flowLatest()
            ).flattenMerge().onEach {
                log.d("last message $it")
            }.bufferedThrottle(100).map { messages ->
                Chat.Service.Messages(
                    account = address,
                    list = messages
                )
            }.onStart {
                log.d("start LastMessageSubscription")
            }.onCompletion {
                log.d("finish LastMessageSubscription $it")
            }.collect(out)
        }
    }
