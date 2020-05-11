package cc.cryptopunks.crypton.module

import android.content.Context
import androidx.room.Room
import cc.cryptopunks.crypton.context.Account
import cc.cryptopunks.crypton.context.Clip
import cc.cryptopunks.crypton.context.Repo
import cc.cryptopunks.crypton.context.SessionRepo
import cc.cryptopunks.crypton.data.Database
import cc.cryptopunks.crypton.migrations
import cc.cryptopunks.crypton.repo.AccountRepo
import cc.cryptopunks.crypton.repo.ClipboardRepo

class RoomRepo(
    context: Context
) : Repo {

    private val database: Database = Room
        .databaseBuilder(context, Database::class.java, "crypton.db")
//        .inMemoryDatabaseBuilder(context, Database::class.java)
        .fallbackToDestructiveMigration()
        .setQueryExecutor(Repo.Context.Query().executor)
        .setTransactionExecutor(Repo.Context.Transaction().executor)
        .addMigrations(*migrations)
        .build()

    override val accountRepo: Account.Repo = AccountRepo(database.accountDao)
    override val clipboardRepo: Clip.Board.Repo = ClipboardRepo()
    override val createSessionRepo: SessionRepo.Factory = RoomSessionRepo.Factory(context)
}
