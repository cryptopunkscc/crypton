package cc.cryptopunks.crypton.factory

import cc.cryptopunks.crypton.context.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import java.util.concurrent.CancellationException

fun RootScope.createSession(address: Address): SessionModule =
    createSession(accountRepo.get(address))


fun RootScope.createSession(account: Account): SessionModule = let {
    val connectionScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    SessionModule(
        rootScope = this,
        address = account.address,
        sessionRepo = createSessionRepo(account.address),
        connection = createConnection(
            Connection.Config(
                scope = connectionScope,
                account = account.address,
                password = account.password
            )
        )
    ) { connectionScope.cancel(CancellationException(it?.message)) }
}
