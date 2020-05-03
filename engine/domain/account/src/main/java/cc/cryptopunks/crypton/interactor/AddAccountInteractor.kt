package cc.cryptopunks.crypton.interactor

import cc.cryptopunks.crypton.context.Account
import cc.cryptopunks.crypton.context.Address
import cc.cryptopunks.crypton.context.Connection
import cc.cryptopunks.crypton.context.Session

internal class AddAccountInteractor(
    private val createConnection: Connection.Factory,
    private val sessionStore: Session.Store,
    private val accountRepo: Account.Repo
) {
    suspend operator fun invoke(
        account: Account,
        register: Boolean
    ) {
        assertAccountNotExist(account.address)
        val scope = Session.Scope()
        val session = Session(
            address = account.address,
            scope = scope,
            connection = createConnection(
                Connection.Config(
                    scope = scope,
                    address = account.address,
                    password = account.password
                )
            )
        ).apply {
            connect()
            if (register) createAccount()
            login()
            initOmemo()
            accountRepo.insert(account)
        }
        sessionStore reduce {
            plus(account.address to session)
        }
    }

    private suspend fun assertAccountNotExist(address: Address) {
        if (accountRepo.contains(address)) throw Account.Exception(
            account = address,
            cause = IllegalArgumentException("Account already exists")
        )
    }
}
