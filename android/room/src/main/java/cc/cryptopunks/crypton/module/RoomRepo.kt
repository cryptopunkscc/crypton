package cc.cryptopunks.crypton.module

import android.content.Context
import androidx.room.Room
import cc.cryptopunks.crypton.context.*
import cc.cryptopunks.crypton.data.Database
import cc.cryptopunks.crypton.repo.*

class RoomRepo(
    context: Context
) : Repo {

    override val queryContext = Repo.Context.Query()
    override val transactionContext = Repo.Context.Transaction()

    private val database: Database = Room
        .databaseBuilder(context, Database::class.java, "crypton.db")
//        .inMemoryDatabaseBuilder(context, Database::class.java)
        .fallbackToDestructiveMigration()
        .setQueryExecutor(queryContext.executor)
        .setTransactionExecutor(transactionContext.executor)
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
        dao = database.messageDao,
        coroutineContext = queryContext
    )

    override val userRepo: User.Repo = UserRepo(
        dao = database.userDao
    )

    override val clipboardRepo: Clip.Board.Repo = ClipboardRepo()
}