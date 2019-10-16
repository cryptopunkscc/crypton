package cc.cryptopunks.crypton.feature.account.manager

import cc.cryptopunks.crypton.api.Client
import cc.cryptopunks.crypton.entity.Account
import cc.cryptopunks.crypton.entity.Address
import cc.cryptopunks.crypton.util.ext.reduce
import java.util.concurrent.atomic.AtomicReference
import javax.inject.Inject

data class AccountManager @Inject constructor(
    private val accountRepo: Account.Repo,
    private val clientManager: Client.Manager
) :
    AtomicReference<Account>(Account.Empty) {

    private val account get() = get()

    suspend fun client(): Client = clientManager.get(account)

    val isInitialized: Boolean get() = clientManager.contains(account)

    fun copy(account: Account): AccountManager = copy().apply { set(account) }

    fun setStatus(status: Account.Status): AccountManager = reduce { copy(status = status) }

    suspend fun load(id: Address): Account = accountRepo.get(id).also { set(it) }

    suspend fun register(): Unit = client().createAccount()

    suspend fun login(): Unit = client().login()

    suspend fun disconnect(): Unit = client().disconnect()

    suspend fun insert(): Account = accountRepo.insert(get()).also { set(it) }

    suspend fun update(): Unit = accountRepo.update(get())

    suspend fun unregister() {
        client().remove()
        delete()
    }

    suspend fun delete() {
        clear()
        accountRepo.delete(get())
    }

    suspend fun clear() {
        clientManager.minus(get())
    }

    inline fun <R> run(
        onAccountException: (Account) -> Any = {},
        block: AccountManager.() -> R
    ): R = try {
        block()
    } catch (throwable: Throwable) {
        onAccountException(get())
        throw get().exception(throwable)
    }
}