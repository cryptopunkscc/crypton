package cc.cryptopunks.crypton.api.model

import cc.cryptopunks.crypton.api.Client
import cc.cryptopunks.crypton.entity.Account
import javax.inject.Inject

class ClientModel @Inject constructor(
    private val createClient: Client.Factory,
    private val clientCache: Client.Cache
) {
    operator fun get(account: Account): Client = account.run {
        clientCache[address.id] ?: createClient(
            Client.Config(
                address = address,
                password = password
            )
        ).also {
            clientCache[address.id] = it
        }
    }

    operator fun contains(account: Account): Boolean =
        account.address.id in clientCache


    operator fun minus(account: Account) {
        clientCache -= account.address.id
    }
}