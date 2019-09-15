package cc.cryptopunks.crypton.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import cc.cryptopunks.crypton.component.DataComponent
import cc.cryptopunks.crypton.entity.Account
import cc.cryptopunks.crypton.entity.Conversation
import cc.cryptopunks.crypton.entity.Message

@Database(
    entities = [
        Account::class,
        Conversation::class,
        Message::class
    ],
    version = 1
)
@TypeConverters(Account.Status.Converter::class)
abstract class Database : RoomDatabase(), DataComponent