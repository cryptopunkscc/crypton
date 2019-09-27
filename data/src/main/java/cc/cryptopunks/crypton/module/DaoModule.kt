package cc.cryptopunks.crypton.module

import cc.cryptopunks.crypton.data.Database
import dagger.Module
import dagger.Provides

@Module
class DaoModule {

    @Provides
    fun Database.accountDao() = accountDao

    @Provides
    fun Database.messageDao() = messageDao

    @Provides
    fun Database.chatDao() = chatDao

    @Provides
    fun Database.userDao() = userDao

    @Provides
    fun Database.chatUserDao() = chatUserDao
}