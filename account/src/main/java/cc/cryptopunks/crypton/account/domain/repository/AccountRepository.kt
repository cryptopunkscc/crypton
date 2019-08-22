package cc.cryptopunks.crypton.account.domain.repository

import cc.cryptopunks.crypton.core.entity.Account
import cc.cryptopunks.crypton.core.util.get
import cc.cryptopunks.crypton.core.util.reduce
import cc.cryptopunks.crypton.api.Client
import java.util.concurrent.atomic.AtomicReference
import javax.inject.Inject

data class AccountRepository @Inject constructor(
    private val dao: Account.Dao,
    private val createClient: Client.Factory,
    private val clientCache: Client.Cache
) :
    AtomicReference<Account>(Account.Empty) {

    val id get() = get().id

    val isInitialized get() = clientCache.contains(get().id)

    val client: Client
        get() = clientCache[id] ?: get {
            createClient(
                Client.Config(
                    id = id,
                    jid = jid,
                    password = credentials.password
                )
            ).also {
                clientCache[id] = it
            }
        }

    operator fun invoke(account: Account) = copy().apply { set(account) }

    fun create() = client.create()

    fun login() = client.login()

    fun disconnect() = client.disconnect()

    fun setStatus(status: Account.Status) {
        reduce { copy(status = status) }
    }

    fun load(id: Long): Account = reduce { dao.get(id) }.get()

    fun insert() = dao.insert(get())!!.also { set(get().copy(id = it)) }

    fun update() = dao.update(get())

    fun delete() {
        client.remove()
        remove()
    }

    fun remove() {
        clear()
        dao.delete(get())
    }

    fun clear() {
        clientCache -= id
    }
}