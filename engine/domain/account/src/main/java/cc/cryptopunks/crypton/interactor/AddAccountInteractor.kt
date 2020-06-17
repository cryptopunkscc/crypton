package cc.cryptopunks.crypton.interactor

import cc.cryptopunks.crypton.context.Account
import cc.cryptopunks.crypton.context.Address
import cc.cryptopunks.crypton.context.AppScope
import cc.cryptopunks.crypton.context.Connection
import cc.cryptopunks.crypton.context.Session
import kotlinx.coroutines.launch

suspend fun AppScope.addAccount(
    account: Account,
    register: Boolean
) {
    log.d("Adding account ${account.address}")
    accountRepo.assertAccountNotExist(account.address)
    val scope = Session.Scope()
    val session = Session(
        address = account.address,
        scope = scope,
        sessionRepo = createSessionRepo(account.address),
        connection = createConnection(
            Connection.Config(
                scope = scope,
                address = account.address,
                password = account.password
            )
        )
    ).apply {
        log.d("Connecting")
        connect()
        log.d("Connected")
        if (register) createAccount()
        login()
        log.d("Logged in")
        scope.launch { initOmemo() }
        accountRepo.insert(account)
        log.d("Account inserted")
    }
    sessionStore reduce {
        plus(account.address to session)
    }
}

private suspend fun Account.Repo.assertAccountNotExist(address: Address) {
    if (contains(address)) throw Account.Exception(
        account = address,
        message = "Account already exists"
    )
}
