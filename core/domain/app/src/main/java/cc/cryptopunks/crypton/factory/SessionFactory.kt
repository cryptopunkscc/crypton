package cc.cryptopunks.crypton.factory

import cc.cryptopunks.crypton.context.*

fun AppScope.createSession(address: Address): SessionModule =
    accountRepo.get(address).let { account ->
        SessionModule(
            appScope = this,
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
