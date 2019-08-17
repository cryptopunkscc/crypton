package cc.cryptopunks.crypton.account.domain.repository

import cc.cryptopunks.crypton.core.entity.Account
import cc.cryptopunks.crypton.core.util.get
import cc.cryptopunks.crypton.core.util.reduce
import cc.cryptopunks.crypton.xmpp.Xmpp
import java.util.concurrent.atomic.AtomicReference
import javax.inject.Inject

data class AccountRepository @Inject constructor(
    private val dao: Account.Dao,
    private val createXmpp: Xmpp.Factory,
    private val xmppCache: Xmpp.Cache
) :
    AtomicReference<Account>(Account.Empty) {

    val id get() = get().id

    val isInitialized get() = xmppCache.contains(get().id)

    val xmpp: Xmpp
        get() = xmppCache[id] ?: get {
            createXmpp(
                Xmpp.Config(
                    id = id,
                    jid = jid,
                    password = credentials.password
                )
            ).also {
                xmppCache[id] = it
            }
        }

    operator fun invoke(account: Account) = copy().apply { set(account) }

    fun create() = xmpp.create()

    fun login() = xmpp.login()

    fun disconnect() = xmpp.disconnect()

    fun setStatus(status: Account.Status) {
        reduce { copy(status = status) }
    }

    fun load(id: Long): Account = reduce { dao.get(id) }.get()

    fun insert() = dao.insert(get())!!.also { set(get().copy(id = it)) }

    fun update() = dao.update(get())

    fun remove() {
        xmpp.remove()
        delete()
    }

    fun delete() {
        clear()
        dao.delete(get())
    }

    fun clear() {
        xmppCache -= id
    }
}