package cc.cryptopunks.crypton.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import cc.cryptopunks.crypton.component.DataComponent
import cc.cryptopunks.crypton.entity.*

@Database(
    entities = [
        Account::class,
        AccountUser::class,
        Chat::class,
        Message::class,
        User::class,
        ChatUser::class
    ],
    version = 1
)
@TypeConverters(Account.Status.Converter::class)
abstract class Database : RoomDatabase(), DataComponent