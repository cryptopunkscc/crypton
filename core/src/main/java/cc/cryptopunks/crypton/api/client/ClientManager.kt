package cc.cryptopunks.crypton.api.client

import cc.cryptopunks.crypton.api.Client
import cc.cryptopunks.crypton.entity.Account
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.Flow

internal class ClientManager(
    private val createClient: Client.Factory,
    private val clientCache: ClientCache
): Client.Manager, Flow<Client> by clientCache {

    override suspend fun get(account: Account): Client = account.run {
        clientCache.get(address.id) ?: createClient(
            Client.Config(
                address = address,
                password = password
            )
        )   .apply { connect() }
            .also { clientCache.put(address.id, it) }
    }

    override suspend fun minus(account: Account) {
        clientCache.remove(account.address.id)?.run { apiScope.cancel() }
    }


    override fun contains(account: Account): Boolean =
        clientCache.contains(account.address.id)
}