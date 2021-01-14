package cc.cryptopunks.crypton.interactor

import cc.cryptopunks.crypton.context.RootScope
import cc.cryptopunks.crypton.context.account
import cc.cryptopunks.crypton.context.accountRepo
import cc.cryptopunks.crypton.context.createSessionScope
import cc.cryptopunks.crypton.context.sessions
import cc.cryptopunks.crypton.logv2.log
import cc.cryptopunks.crypton.logv2.d

internal suspend fun RootScope.loadSessions() {
    val scope = this
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
                .map { createSessionScope(scope, it) }
                .map { it.account.address to it }
        )
    }
}
