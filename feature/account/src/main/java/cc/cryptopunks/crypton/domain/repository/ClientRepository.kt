package cc.cryptopunks.crypton.domain.repository

import cc.cryptopunks.crypton.api.Client
import cc.cryptopunks.crypton.entity.Account
import javax.inject.Inject

class ClientRepository @Inject constructor(
    private val createClient: Client.Factory,
    private val clientCache: Client.Cache
) {
    operator fun get(account: Account): Client = account.run {
        clientCache[id] ?: createClient(
            Client.Config(
                id = id,
                remoteId = remoteId,
                password = credentials.password
            )
        ).also {
            clientCache[id] = it
        }
    }

    operator fun contains(account: Account): Boolean =
        account.id in clientCache


    operator fun minus(account: Account) {
        clientCache -= account.id
    }
}