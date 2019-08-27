package cc.cryptopunks.crypton.core.component

import cc.cryptopunks.kache.core.Kache

interface KacheComponent {
    val provideKache: Kache.Provider
}