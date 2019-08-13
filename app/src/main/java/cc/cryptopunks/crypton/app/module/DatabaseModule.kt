package cc.cryptopunks.crypton.app.module

import android.app.Application
import androidx.room.Room
import cc.cryptopunks.crypton.core.data.CoreDatabase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DatabaseModule {

    @Provides
    @Singleton
    fun appDatabase(context: Application) = Room
//        .databaseBuilder(context, CoreDatabase::class.java, "cryption.db")
        .inMemoryDatabaseBuilder(context, CoreDatabase::class.java)
        .build()
}