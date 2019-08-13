package cc.cryptopunks.crypton.app.util

import cc.cryptopunks.kache.core.Kache
import cc.cryptopunks.kache.core.KacheManager
import cc.cryptopunks.crypton.app.module.GraphScope
import cc.cryptopunks.crypton.app.module.ViewModelScope
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ApplicationCacheProvider @Inject constructor(): Kache.Provider by KacheManager()

@GraphScope
class GraphCacheProvider @Inject constructor(): Kache.Provider by KacheManager()

@ViewModelScope
class ViewModelProvider @Inject constructor(): Kache.Provider by KacheManager()