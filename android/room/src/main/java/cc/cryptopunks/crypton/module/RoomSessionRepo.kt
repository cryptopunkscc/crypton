package cc.cryptopunks.crypton.module

import android.content.Context
import androidx.room.Room
import cc.cryptopunks.crypton.context.*
import cc.cryptopunks.crypton.data.Database
import cc.cryptopunks.crypton.migrations
import cc.cryptopunks.crypton.repo.ChatRepo
import cc.cryptopunks.crypton.repo.MessageRepo
import cc.cryptopunks.crypton.repo.UserRepo

class RoomSessionRepo(
    context: Context,
    address: Address
) : SessionRepo {

    override val queryContext =
        Repo.Context.Query()
    override val transactionContext =
        Repo.Context.Transaction()

    private val database: Database = Room.databaseBuilder(
        context,
        Database::class.java,
        "$address.db"
    )
//        .inMemoryDatabaseBuilder(context, Database::class.java)
        .fallbackToDestructiveMigration()
        .setQueryExecutor(Repo.Context.Query().executor)
        .setTransactionExecutor(Repo.Context.Transaction().executor)
        .addMigrations(*migrations)
        .build()

    override val chatRepo: Chat.Repo =
        ChatRepo(
            chatDao = database.chatDao,
            chatUserDao = database.chatUserDao,
            userDao = database.userDao
        )

    override val messageRepo: Message.Repo =
        MessageRepo(
            dao = database.messageDao,
            coroutineContext = queryContext
        )

    override val userRepo: User.Repo =
        UserRepo(
            dao = database.userDao
        )

    class Factory(
        private val context: Context
    ) : SessionRepo.Factory {
        override fun invoke(
            address: Address
        ): SessionRepo = RoomSessionRepo(
            context = context,
            address = address
        )
    }
}
