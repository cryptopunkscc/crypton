package cc.cryptopunks.crypton.component

import cc.cryptopunks.crypton.entity.*

interface DataComponent {
    val accountDao: Account.Dao
    val chatDao: Chat.Dao
    val messageDao: Message.Dao
    val userDao: User.Dao
    val chatUserDao: ChatUser.Dao
}