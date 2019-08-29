package cc.cryptopunks.crypton.core.util

import android.app.Application
import cc.cryptopunks.kache.android.SharedPreferencesKache
import cc.cryptopunks.kache.core.Kache
import cc.cryptopunks.kache.core.KacheManager
import cc.cryptopunks.kache.core.MemoryKache
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class KacheProviderFactory @Inject constructor(
    private val application: Application
) : () -> Kache.Provider by {
    KacheManager(
        SharedPreferencesKache.Factory { key: Kache.Key<*>, mode: Int ->
            application.getSharedPreferences(key.toString().replace('/', '\\'), mode)
        },
        MemoryKache.Factory
    )
}