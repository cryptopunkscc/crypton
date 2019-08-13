package cc.cryptopunks.crypton.core.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import cc.cryptopunks.crypton.core.entity.Account

@Database(entities = [Account::class], version = 1)
@TypeConverters(Account.Status.Converter::class)
abstract class CoreDatabase : RoomDatabase() {
    abstract fun accountDao(): Account.Dao
}