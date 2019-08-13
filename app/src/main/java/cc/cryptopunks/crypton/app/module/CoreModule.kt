package cc.cryptopunks.crypton.app.module


import cc.cryptopunks.crypton.core.data.CoreDatabase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module(includes = [DatabaseModule::class])
class CoreModule {

    @Provides
    @Singleton
    fun accountDao(coreDatabase: CoreDatabase) = coreDatabase
        .accountDao()
}