package cc.cryptopunks.crypton.component

import cc.cryptopunks.kache.core.Kache

interface KacheComponent {
    val provideKache: Kache.Provider
}