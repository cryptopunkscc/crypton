package cc.cryptopunks.crypton.handler

import cc.cryptopunks.crypton.context.Execute
import cc.cryptopunks.crypton.context.Message
import cc.cryptopunks.crypton.context.Subscribe
import cc.cryptopunks.crypton.handle
import cc.cryptopunks.crypton.selector.filterByStatus
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map

internal fun handleOnMessageExecuteSubscription() =
    handle { _, (command): Subscribe.OnMessageExecute ->
        messageRepo
            .flowLatest(chat.address)
            .filterByStatus(Message.Status.Received, Message.Status.Sent)
            .map(Message::formatArgs)
            .collect { args -> executeSys(Execute(command, args)) }
    }

private fun Message.formatArgs() = listOf(from, to, text).map(Any::toString)
