package cc.cryptopunks.crypton.interactor

import cc.cryptopunks.crypton.context.*
import cc.cryptopunks.crypton.util.typedLog
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

internal class AddAccountInteractor(
    private val createConnection: Connection.Factory,
    private val createRepo: SessionRepo.Factory,
    private val sessionStore: Session.Store,
    private val accountRepo: Account.Repo
) {
    private val log = typedLog()

    suspend operator fun invoke(
        account: Account,
        register: Boolean
    ) = coroutineScope {
        log.d("Adding account ${account.address}")
        assertAccountNotExist(account.address)
        val scope = Session.Scope()
        val session = Session(
            address = account.address,
            scope = scope,
            sessionRepo = createRepo(account.address),
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
            launch { initOmemo() }
            accountRepo.insert(account)
        }
        sessionStore reduce {
            plus(account.address to session)
        }
    }

    private suspend fun assertAccountNotExist(address: Address) {
        if (accountRepo.contains(address)) throw Account.Exception(
            account = address,
            message = "Account already exists"
        )
    }
}
