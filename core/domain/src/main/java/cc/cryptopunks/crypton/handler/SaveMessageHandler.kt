package cc.cryptopunks.crypton.handler

import cc.cryptopunks.crypton.context.Message
import cc.cryptopunks.crypton.context.SessionScope
import cc.cryptopunks.crypton.handle
import cc.cryptopunks.crypton.interactor.saveMessage

internal fun SessionScope.handleSaveMessages() =
    handle<Message.Service.Save> {
        messages.forEach { message ->
            when (message.status) {
                Message.Status.State -> when (message.text) {
                    Message.State.composing.name -> saveMessage(message.copy(id = message.simpleStatusId()))
                    Message.State.paused.name,
                    Message.State.active.name -> messageRepo.delete(id = message.simpleStatusId())
                    else -> Unit
                }
                else -> null
            } ?: saveMessage(message)
        }
    }

private fun Message.simpleStatusId() = "$chat:$from:$status"
