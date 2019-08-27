package cc.cryptopunks.crypton.core.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import cc.cryptopunks.crypton.core.component.DaoComponent
import cc.cryptopunks.crypton.core.entity.Account
import cc.cryptopunks.crypton.core.entity.Conversation
import cc.cryptopunks.crypton.core.entity.Message

@Database(
    entities = [
        Account::class,
        Conversation::class,
        Message::class
    ],
    version = 1
)
@TypeConverters(Account.Status.Converter::class)
abstract class CoreDatabase : RoomDatabase(), DaoComponent