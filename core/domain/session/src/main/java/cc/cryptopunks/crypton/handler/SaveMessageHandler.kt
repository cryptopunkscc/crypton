package cc.cryptopunks.crypton.handler

import cc.cryptopunks.crypton.context.Message
import cc.cryptopunks.crypton.context.SessionScope
import cc.cryptopunks.crypton.context.handle
import cc.cryptopunks.crypton.interactor.saveMessages

internal fun SessionScope.handleSaveMessages() =
    handle<Message.Service.Save> {
        saveMessages(messages)
    }
