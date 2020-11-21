package cc.cryptopunks.crypton.handler

import cc.cryptopunks.crypton.context.Subscribe
import cc.cryptopunks.crypton.handle
import cc.cryptopunks.crypton.interactor.reconnectIfNeeded

internal fun handleReconnectSession() = handle { _, _: Subscribe.ReconnectSession ->
    if (isConnected()) interrupt()
    reconnectIfNeeded(retryCount = -1)
}
