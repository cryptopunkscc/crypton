package cc.cryptopunks.crypton.interactor

import cc.cryptopunks.crypton.context.RootScope
import cc.cryptopunks.crypton.factory.createSession
import cc.cryptopunks.crypton.util.logger.log

internal suspend fun RootScope.loadSessions() {
    sessions.reduce {
        plus(
            accountRepo.addressList()
                .minus(keys)
                .also { accounts ->
                    log.d {
                        if (accounts.isEmpty()) "No accounts found"
                        else "Loading sessions: $accounts"
                    }
                }
                .map { createSession(it) }
                .map { it.account.address to it }
        )
    }
}
