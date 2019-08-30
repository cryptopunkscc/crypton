package cc.cryptopunks.crypton.module

import cc.cryptopunks.crypton.util.KacheProviderFactory
import cc.cryptopunks.kache.core.Kache
import cc.cryptopunks.kache.core.KacheManager
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class KacheModule {

    @Provides
    @Singleton
    fun kacheManager(provide: KacheProviderFactory): KacheManager = provide() as KacheManager

    @Provides
    @Singleton
    fun kacheProvider(kacheManager: KacheManager): Kache.Provider = kacheManager
}