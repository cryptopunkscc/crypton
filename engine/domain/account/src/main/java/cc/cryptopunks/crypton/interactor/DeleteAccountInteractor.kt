package cc.cryptopunks.crypton.interactor

import cc.cryptopunks.crypton.context.Account
import cc.cryptopunks.crypton.context.Address
import cc.cryptopunks.crypton.context.Session
import kotlinx.coroutines.cancel

class DeleteAccountInteractor internal constructor(
    private val sessionStore: Session.Store,
    private val accountRepo: Account.Repo
) {
    suspend operator fun invoke(address: Address, unregister: Boolean) {
        sessionStore.apply {
            get()[address]?.run {
                if (unregister)
                    removeAccount() else
                    disconnect()
                reduce { minus(address) }
                scope.cancel()
            }
        }
        accountRepo.delete(address)
    }
}
