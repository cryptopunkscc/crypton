package cc.cryptopunks.crypton.interactor

import cc.cryptopunks.crypton.context.Account
import cc.cryptopunks.crypton.context.Address
import cc.cryptopunks.crypton.context.RootScope
import cc.cryptopunks.crypton.factory.createSession
import cc.cryptopunks.crypton.util.Log
import cc.cryptopunks.crypton.util.logger.CoroutineLog
import cc.cryptopunks.crypton.util.logger.log
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

suspend fun RootScope.addAccount(
    account: Account,
    register: Boolean,
    insert: Boolean
) {
    log.d { "Adding account ${account.address}" }
    accountRepo.assertAccountNotExist(account.address)
    val logInfo = coroutineScope {
        coroutineContext[CoroutineLog.Action]!! + CoroutineLog.Status(Log.Event.Status.Handling)
    }
    createSession(account).apply {
        launch(logInfo) {
            log.d { "Connecting" }
            connect()
            log.d { "Connected" }
            if (register) createAccount()
            login()
            log.d { "Logged in" }
            launch { initOmemo() }
            if (insert) accountRepo.insert(account)
            log.d { "Account inserted" }
        }.join()
    }.also { session ->
        sessions reduce {
            plus(account.address to session)
        }
    }
}

private suspend fun Account.Repo.assertAccountNotExist(address: Address) {
    if (contains(address)) throw Account.Exception(
        account = address,
        message = "Account already exists"
    )
}
