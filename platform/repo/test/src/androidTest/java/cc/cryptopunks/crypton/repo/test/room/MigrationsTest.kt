package cc.cryptopunks.crypton.repo.test.room

import androidx.room.Room
import androidx.room.testing.MigrationTestHelper
import androidx.sqlite.db.framework.FrameworkSQLiteOpenHelperFactory
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import cc.cryptopunks.crypton.room.internal.Database
import cc.cryptopunks.crypton.room.internal.migrations
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class MigrationsTest {

    companion object {
        private const val TEST_DB = "migration-test"
        private val DATABASE_CLASS = Database::class.java
        private val instrumentation get() = InstrumentationRegistry.getInstrumentation()
    }

    @get:Rule
    val helper = MigrationTestHelper(
        instrumentation,
        DATABASE_CLASS.canonicalName,
        FrameworkSQLiteOpenHelperFactory()
    )

    @Test
    @Throws(IOException::class)
    fun migrateAll() {
        helper.createDatabase(TEST_DB, 1).apply {
            close()
        }
        Room.databaseBuilder(
            instrumentation.targetContext,
            DATABASE_CLASS,
            TEST_DB
        ).addMigrations(*migrations).build().apply {
            openHelper.writableDatabase
            close()
        }
    }
}
