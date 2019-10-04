package cc.cryptopunks.crypton.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import cc.cryptopunks.crypton.entity.*

@Database(
    entities = [
        AccountData::class,
        ChatData::class,
        MessageData::class,
        UserData::class,
        ChatUserData::class
    ],
    version = 1
)
@TypeConverters(AccountData.StatusConverter::class)
internal abstract class Database : RoomDatabase() {
    abstract val accountDao: AccountData.Dao
    abstract val chatDao: ChatData.Dao
    abstract val userDao: UserData.Dao
    abstract val chatUserDao: ChatUserData.Dao
    abstract val messageDao: MessageData.Dao
}