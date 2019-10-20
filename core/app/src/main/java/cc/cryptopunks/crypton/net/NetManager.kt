package cc.cryptopunks.crypton.net

import cc.cryptopunks.crypton.entity.Account
import cc.cryptopunks.crypton.util.log
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.Flow

internal class NetManager(
    private val createNet: Net.Factory,
    private val netCache: NetCache
) : Net.Manager, Flow<Net> by netCache {

    override val isEmpty: Boolean get() = netCache.isEmpty

    override suspend fun get(account: Account): Net = account.run {
        synchronized(this@NetManager) {
            netCache.get(address.id) ?: createNet(
                Net.Config(
                    address = address,
                    password = password
                )
            ).apply {
                log<NetManager>("get account")
                provider.net<Account.Net>().connect()
            }
        }.also {
            netCache.put(address.id, it)
        }
    }

    override suspend fun minus(account: Account) {
        netCache.remove(account.address.id)?.run { apiScope.cancel() }
    }


    override fun contains(account: Account): Boolean =
        netCache.contains(account.address.id)
}