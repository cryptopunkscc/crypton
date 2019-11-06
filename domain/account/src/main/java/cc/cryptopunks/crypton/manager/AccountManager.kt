package cc.cryptopunks.crypton.manager

import cc.cryptopunks.crypton.entity.Account
import cc.cryptopunks.crypton.entity.Address
import cc.cryptopunks.crypton.util.ext.reduce
import java.util.concurrent.atomic.AtomicReference
import javax.inject.Inject

data class AccountManager @Inject constructor(
    private val accountRepo: Account.Repo,
    private val sessionManager: SessionManager
) :
    AtomicReference<Account>(Account.Empty) {

    private val account get() = get()

    private fun api(): Account.Net = sessionManager[account]

    val isInitialized: Boolean get() = account in sessionManager

    fun copy(account: Account): AccountManager = copy().apply { set(account) }

    fun setStatus(status: Account.Status): AccountManager = reduce { copy(status = status) }

    suspend fun load(id: Address): Account = accountRepo.get(id).also { set(it) }

    suspend fun connect(): Unit = api().connect()

    suspend fun register(): Unit = api().createAccount()

    suspend fun login(): Unit = api().login()

    suspend fun initOmemo() {
        println("init omemo start")
        api().initOmemo()
        println("init omemo stop")
    }

    suspend fun disconnect(): Unit = api().disconnect()

    suspend fun insert(): Account = accountRepo.insert(get()).also { set(it) }

    suspend fun update(): Unit = accountRepo.update(get())

    suspend fun unregister() {
        api().remove()
        delete()
    }

    suspend fun delete() {
        clear()
        accountRepo.delete(get())
    }

    suspend fun clear() {
        sessionManager.minus(get())
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