package cc.cryptopunks.crypton.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import cc.cryptopunks.crypton.component.DataComponent
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
abstract class Database : RoomDatabase(), DataComponent