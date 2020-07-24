package cc.cryptopunks.crypton.factory

import cc.cryptopunks.crypton.context.*

fun RootScope.createSession(address: Address): SessionModule =
    accountRepo.get(address).let { account ->
        SessionModule(
            rootScope = this,
            address = address,
            sessionRepo = createSessionRepo(address),
            connection = createConnection(
                Connection.Config(
                    scope = this,
                    account = account.address,
                    password = account.password
                )
            )
        )
    }
