package cc.cryptopunks.crypton.module


import android.app.Application
import androidx.room.Room
import cc.cryptopunks.crypton.data.Database
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module(includes = [DaoModule::class])
class DataModule {

    @Provides
    @Singleton
    fun appDatabase(context: Application): Database = Room
        .databaseBuilder(context, Database::class.java, "crypton.db")
//        .inMemoryDatabaseBuilder(context, Database::class.java)
        .build()
}

