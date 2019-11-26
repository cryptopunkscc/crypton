package cc.cryptopunks.crypton.manager

import cc.cryptopunks.crypton.entity.Account
import cc.cryptopunks.crypton.entity.Address
import javax.inject.Inject

data class AccountManager @Inject constructor(
    private val accountRepo: Account.Repo,
    private val sessionManager: SessionManager
) {

    var account: Address = Address.Empty
        private set

    val isInitialized get() = account in sessionManager

    val session get() = sessionManager[account]

    val isConnected get() = session.isConnected()

    val isAuthenticated get() = session.isAuthenticated()

    fun copy(account: Address): AccountManager = copy().apply {
        this.account = account
    }

    fun connect(): Unit = session.connect()

    fun register(): Unit = session.createAccount()

    fun login(): Unit = session.login()

    fun initOmemo() = session.initOmemo()

    fun disconnect(): Unit = session.disconnect()

    suspend fun insert(account: Account) {
        accountRepo.insert(account)
        this.account = account.address
    }

    suspend fun unregister() {
        session.removeAccount()
        delete()
    }

    suspend fun delete() {
        clear()
        accountRepo.delete(account)
    }

    private fun clear() {
        sessionManager.minus(account)
    }

    inline fun <R> run(
        onAccountException: (Address) -> Any = {},
        block: AccountManager.() -> R
    ): R = try {
        block()
    } catch (throwable: Throwable) {
        onAccountException(account)
        throw Account.Exception(account, throwable)
    }

    suspend fun all() = accountRepo.addressList().map { copy(it) }
}