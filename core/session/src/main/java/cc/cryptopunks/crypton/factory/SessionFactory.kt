package cc.cryptopunks.crypton.factory

import cc.cryptopunks.crypton.entity.Account
import cc.cryptopunks.crypton.entity.Session
import cc.cryptopunks.crypton.net.Net
import javax.inject.Inject

class SessionFactory @Inject constructor(
    createNet: Net.Factory
) : (Account) -> Session by { account ->
    val scope = Session.Scope()
    val config = Net.Config(
        scope = scope,
        address = account.address,
        password = account.password
    )
    Session(
        address = account.address,
        scope = scope,
        net = createNet(config)
    )
}