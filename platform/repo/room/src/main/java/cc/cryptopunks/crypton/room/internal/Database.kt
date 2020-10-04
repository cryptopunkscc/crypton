package cc.cryptopunks.crypton.room.internal

import androidx.room.Database
import androidx.room.RoomDatabase
import cc.cryptopunks.crypton.entity.AccountData
import cc.cryptopunks.crypton.entity.ChatData
import cc.cryptopunks.crypton.entity.ChatUserData
import cc.cryptopunks.crypton.entity.FingerprintData
import cc.cryptopunks.crypton.entity.MessageData
import cc.cryptopunks.crypton.entity.UserData

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
abstract class Database : RoomDatabase() {
    abstract val accountDao: AccountData.Dao
    abstract val chatDao: ChatData.Dao
    abstract val userDao: UserData.Dao
    abstract val chatUserDao: ChatUserData.Dao
    abstract val messageDao: MessageData.Dao
    abstract val fingerprintDao: FingerprintData.Dao
}
