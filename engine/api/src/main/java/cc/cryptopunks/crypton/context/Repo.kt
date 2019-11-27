package cc.cryptopunks.crypton.context

interface Repo {
    val accountRepo: Account.Repo
    val chatRepo: Chat.Repo
    val messageRepo: Message.Repo
    val userRepo: User.Repo
}