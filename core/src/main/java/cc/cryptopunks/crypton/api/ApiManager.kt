package cc.cryptopunks.crypton.api

import cc.cryptopunks.crypton.entity.Account
import cc.cryptopunks.crypton.util.log
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.Flow

internal class ApiManager(
    private val createApi: Api.Factory,
    private val apiCache: ApiCache
) : Api.Manager, Flow<Api> by apiCache {

    override val isEmpty: Boolean get() = apiCache.isEmpty

    override suspend fun get(account: Account): Api = account.run {
        synchronized(this@ApiManager) {
            apiCache.get(address.id) ?: createApi(
                Api.Config(
                    address = address,
                    password = password
                )
            ).apply {
                log<ApiManager>("get account")
                provider.api<Account.Api>().connect()
            }
        }.also {
            apiCache.put(address.id, it)
        }
    }

    override suspend fun minus(account: Account) {
        apiCache.remove(account.address.id)?.run { apiScope.cancel() }
    }


    override fun contains(account: Account): Boolean =
        apiCache.contains(account.address.id)
}