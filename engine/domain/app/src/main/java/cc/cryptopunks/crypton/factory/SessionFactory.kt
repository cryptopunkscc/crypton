package cc.cryptopunks.crypton.factory

import cc.cryptopunks.crypton.context.*

class SessionFactory(
    accountRepo: Account.Repo,
    createConnection: Connection.Factory,
    createSessionRepo: SessionRepo.Factory
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
        connection = createConnection(config),
        sessionRepo = createSessionRepo(account.address)
    )
}
