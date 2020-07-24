package cc.cryptopunks.crypton.handler

import cc.cryptopunks.crypton.context.Exec
import cc.cryptopunks.crypton.handle
import cc.cryptopunks.crypton.service.startSessionService

internal fun handleStartSessionService() = handle { _, _: Exec.SessionService ->
    log.d("Handle StartServices $address")
    startSessionService()
}
