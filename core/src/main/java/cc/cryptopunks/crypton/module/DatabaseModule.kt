package cc.cryptopunks.crypton.module


import android.app.Application
import androidx.room.Room
import cc.cryptopunks.crypton.data.CoreDatabase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DatabaseModule {

    @Provides
    @Singleton
    fun appDatabase(context: Application): CoreDatabase = Room
        .databaseBuilder(context, CoreDatabase::class.java, "cryption.db")
//        .inMemoryDatabaseBuilder(context, CoreDatabase::class.java)
        .build()

    @Provides
    fun CoreDatabase.accountDao() = accountDao

    @Provides
    fun CoreDatabase.conversationDao() = conversationDao

    @Provides
    fun CoreDatabase.messageDao() = messageDao

}