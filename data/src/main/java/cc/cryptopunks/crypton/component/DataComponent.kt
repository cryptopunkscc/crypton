package cc.cryptopunks.crypton.component

import cc.cryptopunks.crypton.entity.*

interface DataComponent {
    val accountDao: AccountData.Dao
    val chatDao: ChatData.Dao
    val userDao: UserData.Dao
    val chatUserDao: ChatUserData.Dao
    val messageDao: MessageData.Dao
}