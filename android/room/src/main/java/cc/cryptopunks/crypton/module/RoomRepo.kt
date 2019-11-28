package cc.cryptopunks.crypton.module

import android.content.Context
import androidx.room.Room
import cc.cryptopunks.crypton.context.*
import cc.cryptopunks.crypton.data.Database
import cc.cryptopunks.crypton.repo.AccountRepo
import cc.cryptopunks.crypton.repo.ChatRepo
import cc.cryptopunks.crypton.repo.MessageRepo
import cc.cryptopunks.crypton.repo.UserRepo

class RoomRepo(
    context: Context
) : Repo {

    private val database: Database = Room
        .databaseBuilder(context, Database::class.java, "crypton.db")
//        .inMemoryDatabaseBuilder(context, Database::class.java)
        .fallbackToDestructiveMigration()
        .build()

    override val accountRepo: Account.Repo = AccountRepo(
        dao = database.accountDao
    )

    override val chatRepo: Chat.Repo = ChatRepo(
        chatDao = database.chatDao,
        chatUserDao = database.chatUserDao,
        userDao = database.userDao
    )

    override val messageRepo: Message.Repo = MessageRepo(
        dao = database.messageDao
    )

    override val userRepo: User.Repo = UserRepo(
        dao = database.userDao
    )
}