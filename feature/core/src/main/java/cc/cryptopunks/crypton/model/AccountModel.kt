package cc.cryptopunks.crypton.model

import cc.cryptopunks.crypton.api.Client
import cc.cryptopunks.crypton.entity.Account
import cc.cryptopunks.crypton.entity.Address
import cc.cryptopunks.crypton.util.ext.reduce
import java.util.concurrent.atomic.AtomicReference
import javax.inject.Inject

data class AccountModel @Inject constructor(
    private val repo: Account.Repo,
    private val clientModel: ClientModel
) :
    AtomicReference<Account>(Account.Empty) {

    val isInitialized get() = get() in clientModel

    val client: Client get() = clientModel[get()]

    fun copy(account: Account) = copy().apply { set(account) }

    fun register(): Unit = client.create()

    fun login(): Unit = client.login()

    fun disconnect(): Unit = client.disconnect()

    fun setStatus(status: Account.Status) {
        reduce { copy(status = status) }
    }

    fun load(id: Address): Account = reduce { repo.get(id) }.get()

    fun insert(): Account = repo.insert(get()).also { set(it) }

    fun update(): Unit = repo.update(get())

    fun unregister() {
        client.remove()
        delete()
    }

    fun delete() {
        clear()
        repo.delete(get())
    }

    fun clear() {
        clientModel - get()
    }

    inline fun <R> run(block: AccountModel.() -> R): R =
        try {
            block()
        } catch (throwable: Throwable) {
            throw get().exception(throwable)
        }
}