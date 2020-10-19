package cc.cryptopunks.crypton.room

import android.content.Context
import androidx.room.Room
import cc.cryptopunks.crypton.context.Address
import cc.cryptopunks.crypton.context.Chat
import cc.cryptopunks.crypton.context.Device
import cc.cryptopunks.crypton.context.Message
import cc.cryptopunks.crypton.context.Repo
import cc.cryptopunks.crypton.context.Roster
import cc.cryptopunks.crypton.context.SessionRepo
import cc.cryptopunks.crypton.room.internal.Database
import cc.cryptopunks.crypton.room.internal.migrations
import cc.cryptopunks.crypton.repo.ChatRepo
import cc.cryptopunks.crypton.repo.DeviceRepo
import cc.cryptopunks.crypton.repo.MessageRepo
import cc.cryptopunks.crypton.repo.RosterRepo

class RoomSessionRepo(
    context: Context,
    address: Address
) : SessionRepo {

    override val queryContext = Repo.Context.Query()
    override val transactionContext = Repo.Context.Transaction()

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

    override val rosterRepo: Roster.Repo =
        RosterRepo(
            dao = database.userDao
        )

    override val deviceRepo: Device.Repo =
        DeviceRepo(
            dao = database.fingerprintDao
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
