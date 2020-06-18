package cc.cryptopunks.crypton.selector

import cc.cryptopunks.crypton.context.Net
import cc.cryptopunks.crypton.context.SessionScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart

internal fun SessionScope.omemoInitializations(): Flow<Net.OmemoInitialized> =
    netEvents()
        .onStart { log.d("Start") }
        .filterIsInstance<Net.OmemoInitialized>()
        .onEach { log.d("Omemo initialized") }
