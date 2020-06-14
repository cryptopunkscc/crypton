package cc.cryptopunks.crypton.selector

import cc.cryptopunks.crypton.context.Net
import cc.cryptopunks.crypton.util.typedLog
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart

internal class OmemoInitializationsSelector(private val net: Net) {
    private val log = typedLog()
    operator fun invoke(): Flow<Net.OmemoInitialized> = net
        .netEvents()
        .onStart { log.d("Start") }
        .filterIsInstance<Net.OmemoInitialized>()
        .onEach { log.d("Omemo initialized") }
}
