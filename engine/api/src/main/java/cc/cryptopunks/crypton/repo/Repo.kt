package cc.cryptopunks.crypton.repo

import cc.cryptopunks.crypton.context.Account
import cc.cryptopunks.crypton.context.Chat
import cc.cryptopunks.crypton.context.Message
import cc.cryptopunks.crypton.context.User

interface Repo {
    val accountRepo: Account.Repo
    val chatRepo: Chat.Repo
    val messageRepo: Message.Repo
    val userRepo: User.Repo
}