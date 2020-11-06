package cc.cryptopunks.crypton.handler

import cc.cryptopunks.crypton.context.Account
import cc.cryptopunks.crypton.context.Exec
import cc.cryptopunks.crypton.context.isConference
import cc.cryptopunks.crypton.interactor.createChat
import cc.cryptopunks.crypton.handle

internal fun handleCreateChat() = handle { output, (chat): Exec.CreateChat ->
    sessions[chat.account]!!.createChat(chat)
    if (!chat.address.isConference && !iAmSubscribed(chat.address))
        subscribe(chat.address)
    output(Account.ChatCreated(chat.address))
}
