package cc.cryptopunks.crypton.module

import cc.cryptopunks.crypton.data.Database
import dagger.Module
import dagger.Provides

@Module
class DaoModule {

    @Provides
    fun Database.accountDao() = accountDao

    @Provides
    fun Database.conversationDao() = conversationDao

    @Provides
    fun Database.messageDao() = messageDao

}