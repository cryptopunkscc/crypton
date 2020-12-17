package cc.cryptopunks.crypton.interactor

import cc.cryptopunks.crypton.context.Account
import cc.cryptopunks.crypton.context.Address
import cc.cryptopunks.crypton.context.RootScope
import cc.cryptopunks.crypton.context.accountNet
import cc.cryptopunks.crypton.context.accountRepo
import cc.cryptopunks.crypton.context.createSessionScope
import cc.cryptopunks.crypton.context.net
import cc.cryptopunks.crypton.context.sessions
import cc.cryptopunks.crypton.util.Log
import cc.cryptopunks.crypton.util.logger.CoroutineLog
import cc.cryptopunks.crypton.util.logger.log
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

suspend fun RootScope.addAccount(
    account: Account,
    register: Boolean,
    insert: Boolean
) {
    log.d { "Adding account ${account.address}" }
    val accountRepo = accountRepo
    accountRepo.assertAccountNotExist(account.address)
    val logInfo = coroutineScope {
        coroutineContext[CoroutineLog.Action]!! + CoroutineLog.Status(Log.Event.Status.Handling)
    }
    createSessionScope(account).apply {
        withContext(logInfo) {
            val net = net
            log.d { "Connecting" }
            net.connect()
            log.d { "Connected" }
            accountNet.run {
                if (register) createAccount()
                login()
            }
            log.d { "Logged in" }
            launch { net.initOmemo() }
            if (insert) accountRepo.insert(account)
            log.d { "Account inserted" }
        }
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
