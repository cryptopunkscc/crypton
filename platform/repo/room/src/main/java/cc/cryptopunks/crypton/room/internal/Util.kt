package cc.cryptopunks.crypton.room.internal

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

internal fun migration(
    from: Int,
    to: Int,
    migrate: SupportSQLiteDatabase.() -> Unit
): Migration = object : Migration(from, to) {
    override fun migrate(database: SupportSQLiteDatabase) = database.migrate()
}
