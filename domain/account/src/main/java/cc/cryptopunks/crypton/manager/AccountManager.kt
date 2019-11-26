package cc.cryptopunks.crypton.manager

import cc.cryptopunks.crypton.entity.Account
import java.util.concurrent.atomic.AtomicReference
import javax.inject.Inject

data class AccountManager @Inject constructor(
    private val accountRepo: Account.Repo,
    private val sessionManager: SessionManager
) :
    AtomicReference<Account>(Account.Empty) {

    private val account get() = get()

    val isInitialized get() = account in sessionManager

    val session get() = sessionManager[account]

    val isConnected get() = session.isConnected()

    val isAuthenticated get() = session.isAuthenticated()

    fun copy(account: Account): AccountManager = copy().apply { set(account) }

    fun connect(): Unit = session.connect()

    fun register(): Unit = session.createAccount()

    fun login(): Unit = session.login()

    fun initOmemo() = session.initOmemo()

    fun disconnect(): Unit = session.disconnect()

    suspend fun insert(): Account = accountRepo.insert(get()).also { set(it) }

    suspend fun update(): Unit = accountRepo.update(get())

    suspend fun unregister() {
        session.removeAccount()
        delete()
    }

    suspend fun delete() {
        clear()
        accountRepo.delete(get())
    }

    private fun clear() {
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

    suspend fun all() = accountRepo.list().map { copy(it) }
}