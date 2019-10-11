package cc.cryptopunks.crypton.api.client

import cc.cryptopunks.crypton.api.Client
import cc.cryptopunks.crypton.entity.Account
import kotlinx.coroutines.flow.Flow

internal class ClientManager(
    private val createClient: Client.Factory,
    private val clientCache: ClientCache
): Client.Manager, Flow<Client> by clientCache {

    override fun get(account: Account): Client = synchronized(this) {
        account.run {
            clientCache[address.id] ?: createClient(
                Client.Config(
                    address = address,
                    password = password
                )
            )   .apply { connect() }
                .also { clientCache[address.id] = it }
        }
    }

    override fun contains(account: Account): Boolean = synchronized(this) {
        account.address.id in clientCache
    }


    override fun minus(account: Account) = synchronized(this) {
        clientCache -= account.address.id
    }
}