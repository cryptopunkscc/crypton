package cc.cryptopunks.crypton.data

import androidx.room.Database
import androidx.room.RoomDatabase
import cc.cryptopunks.crypton.entity.*

@Database(
    entities = [
        AccountData::class,
        ChatData::class,
        MessageData::class,
        UserData::class,
        ChatUserData::class,
        FingerprintData::class
    ],
    version = 1
)
internal abstract class Database : RoomDatabase() {
    abstract val accountDao: AccountData.Dao
    abstract val chatDao: ChatData.Dao
    abstract val userDao: UserData.Dao
    abstract val chatUserDao: ChatUserData.Dao
    abstract val messageDao: MessageData.Dao
    abstract val fingerprintDao: FingerprintData.Dao
}
