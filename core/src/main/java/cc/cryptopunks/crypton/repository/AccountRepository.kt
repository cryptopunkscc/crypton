package cc.cryptopunks.crypton.repository

import cc.cryptopunks.crypton.api.Client
import cc.cryptopunks.crypton.entity.Account
import cc.cryptopunks.crypton.util.ext.reduce
import java.util.concurrent.atomic.AtomicReference
import javax.inject.Inject

data class AccountRepository @Inject constructor(
    private val dao: Account.Dao,
    private val clientRepository: ClientRepository
) :
    AtomicReference<Account>(Account.Empty) {

    val isInitialized get() = get() in clientRepository

    val client: Client get() = clientRepository[get()]

    fun copy(account: Account) = copy().apply { set(account) }

    fun register(): Unit = client.create()

    fun login(): Unit = client.login()

    fun disconnect(): Unit = client.disconnect()

    fun setStatus(status: Account.Status) {
        reduce { copy(status = status) }
    }

    fun load(id: Long): Account = reduce { dao.get(id) }.get()

    fun insert(): Long = dao.insert(get())!!.also { set(get().copy(id = it)) }

    fun update(): Unit = dao.update(get())

    fun unregister() {
        client.remove()
        delete()
    }

    fun delete() {
        clear()
        dao.delete(get())
    }

    fun clear() {
        clientRepository - get()
    }

    inline fun <R> run(block: AccountRepository.() -> R): R =
        try {
            block()
        } catch (throwable: Throwable) {
            throw get().exception(throwable)
        }
}