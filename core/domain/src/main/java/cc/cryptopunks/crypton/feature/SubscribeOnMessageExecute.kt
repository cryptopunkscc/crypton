package cc.cryptopunks.crypton.feature

import cc.cryptopunks.crypton.cliv2.command
import cc.cryptopunks.crypton.cliv2.config
import cc.cryptopunks.crypton.cliv2.param
import cc.cryptopunks.crypton.context.Execute
import cc.cryptopunks.crypton.context.Message
import cc.cryptopunks.crypton.context.Subscribe
import cc.cryptopunks.crypton.context.chat
import cc.cryptopunks.crypton.context.executeSys
import cc.cryptopunks.crypton.context.messageRepo
import cc.cryptopunks.crypton.factory.handler
import cc.cryptopunks.crypton.feature
import cc.cryptopunks.crypton.inScope
import cc.cryptopunks.crypton.selector.filterByStatus
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map

internal fun subscribeOnMessageExecute() = feature(

    command(
        config("account"),
        config("chat"),
        param().copy(name = "command"),
        name = "subscribe on message",
        description = "Register subscription which will execute given command when any new message arrive in chat scope."
    ) { (account, chat, command) ->
        Subscribe.OnMessageExecute(command).inScope(account, chat)
    },

    handler = handler { _, (command): Subscribe.OnMessageExecute ->
        messageRepo
            .flowLatest(chat.address)
            .filterByStatus(Message.Status.Received, Message.Status.Sent)
            .map(Message::formatArgs)
            .collect { args -> executeSys(Execute(command, args)) }
    }
)

private fun Message.formatArgs() = listOf(from, to, body).map(Any::toString)
