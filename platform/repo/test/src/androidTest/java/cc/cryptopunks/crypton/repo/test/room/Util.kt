package cc.cryptopunks.crypton.repo.test.room

import android.content.Context
import androidx.room.Room
import cc.cryptopunks.crypton.context.Repo
import cc.cryptopunks.crypton.room.internal.Database
import cc.cryptopunks.crypton.room.internal.migrations

fun roomTestDatabase(context: Context) = Room
    .inMemoryDatabaseBuilder(context, Database::class.java)
    .fallbackToDestructiveMigration()
    .setQueryExecutor(Repo.Context.Query().executor)
    .setTransactionExecutor(Repo.Context.Transaction().executor)
    .addMigrations(*migrations)
    .build()
