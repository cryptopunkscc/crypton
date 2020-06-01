package cc.cryptopunks.crypton.handler

import cc.cryptopunks.crypton.context.Chat
import cc.cryptopunks.crypton.context.Session
import cc.cryptopunks.crypton.module.handle
import cc.cryptopunks.crypton.util.ext.bufferedThrottle
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

fun Session.subscribeLastMessage() = handle<Chat.Service.Subscribe.LastMessage> { output ->
    scope.launch {
        flowOf(
            messageRepo.list().asFlow(),
            messageRepo.flowLatest()
        ).flattenConcat().bufferedThrottle(100).map { messages ->
            Chat.Service.Messages(
                account = address,
                list = messages
            )
        }.collect(output)
    }
}
private const val SEVEN_DAYS_MILLIS = 1000 * 60 * 60 * 24 * 7
