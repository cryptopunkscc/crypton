package cc.cryptopunks.crypton.factory

import cc.cryptopunks.crypton.entity.Account
import cc.cryptopunks.crypton.entity.Session
import cc.cryptopunks.crypton.connection.Connection
import cc.cryptopunks.crypton.entity.Address
import javax.inject.Inject

class SessionFactory @Inject constructor(
    createConnection: Connection.Factory,
    accountRepo: Account.Repo
) : (Address) -> Session by { address ->
    val scope = Session.Scope()
    val account = accountRepo.get(address)
    val config = Connection.Config(
        scope = scope,
        address = account.address,
        password = account.password
    )
    Session(
        address = account.address,
        scope = scope,
        connection = createConnection(config)
    )
}