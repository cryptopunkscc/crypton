package cc.cryptopunks.crypton.component

import cc.cryptopunks.crypton.entity.Account
import cc.cryptopunks.crypton.entity.Chat
import cc.cryptopunks.crypton.entity.Message
import cc.cryptopunks.crypton.entity.User

interface RepoComponent {
    val accountRepo: Account.Repo
    val chatRepo: Chat.Repo
    val messageRepo: Message.Repo
    val userRepo: User.Repo
}