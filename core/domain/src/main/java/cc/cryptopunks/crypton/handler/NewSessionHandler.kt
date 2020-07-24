package cc.cryptopunks.crypton.handler

import cc.cryptopunks.crypton.context.Account
import cc.cryptopunks.crypton.handle
import cc.cryptopunks.crypton.service.startSessionService
import kotlinx.coroutines.launch

internal fun handleStartServices() = handle { _, _: Account.Service.StartServices ->
    log.d("Handle StartServices $address")
    launch { startSessionService() }
}
