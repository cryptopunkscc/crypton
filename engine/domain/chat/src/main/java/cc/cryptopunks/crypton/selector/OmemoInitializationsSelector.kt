package cc.cryptopunks.crypton.selector

import cc.cryptopunks.crypton.context.Net
import kotlinx.coroutines.flow.filterIsInstance

internal class OmemoInitializationsSelector(private val net: Net) {
    operator fun invoke() = net
        .netEvents()
        .filterIsInstance<Net.OmemoInitialized>()
}
