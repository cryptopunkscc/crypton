package cc.cryptopunks.crypton.interactor

import cc.cryptopunks.crypton.context.Account
import cc.cryptopunks.crypton.context.Session
import cc.cryptopunks.crypton.factory.SessionFactory
import cc.cryptopunks.crypton.util.typedLog

internal class LoadSessionsInteractor(
    private val accountRepo: Account.Repo,
    private val createSession: SessionFactory,
    private val sessionStore: Session.Store
) {
    private val log = typedLog()

    suspend operator fun invoke() {
        sessionStore.reduce {
            plus(
                accountRepo.addressList()
                    .minus(keys)
                    .map(createSession)
                    .map { it.address to it }
            )
        }?.also {
            log.d("Load sessions ${it.keys}")
        }
    }
}
