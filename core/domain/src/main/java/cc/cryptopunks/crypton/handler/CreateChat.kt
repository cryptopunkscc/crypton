package cc.cryptopunks.crypton.handler

import cc.cryptopunks.crypton.context.Account
import cc.cryptopunks.crypton.context.Exec
import cc.cryptopunks.crypton.context.createChat
import cc.cryptopunks.crypton.handle

internal fun handleCreateChat() = handle { output, (chat): Exec.CreateChat ->
    sessions[chat.account]!!.createChat(chat)
    output(Account.ChatCreated(chat.address))
}
