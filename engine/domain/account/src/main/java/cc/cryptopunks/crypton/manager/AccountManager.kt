package cc.cryptopunks.crypton.manager

import cc.cryptopunks.crypton.context.Account
import cc.cryptopunks.crypton.context.Address
import cc.cryptopunks.crypton.context.Session
import javax.inject.Inject

data class AccountManager(
    val address: Address,
    private val accountRepo: Account.Repo,
    private val sessionManager: SessionManager

) {
    val session: Session get() = sessionManager[address]

    val isInitialized: Boolean get() = address in sessionManager

    val isConnected: Boolean get() = session.isConnected()

    val isAuthenticated: Boolean get() = session.isAuthenticated()

    fun connect(): Unit = session.connect()

    fun disconnect(): Unit = session.disconnect()

    fun register(): Unit = session.createAccount()

    fun unregister(): Unit = session.removeAccount()

    fun login(): Unit = session.login()

    fun initOmemo(): Unit = session.initOmemo()

    fun clear(): Unit = sessionManager.minus(address)

    suspend fun insert(password: CharSequence) = accountRepo.insert(Account(address, password))

    suspend fun delete(): Unit = accountRepo.delete(address)

    suspend fun all() = accountRepo.addressList().map { copy(address = it) }

    inline fun <R> run(
        onAccountException: (Address) -> Any = {},
        block: AccountManager.() -> R
    ): R = try {
        block()
    } catch (throwable: Throwable) {
        onAccountException(address)
        throw Account.Exception(address, throwable)
    }


    @Inject
    constructor(
        accountRepo: Account.Repo,
        sessionManager: SessionManager
    ) : this(
        accountRepo = accountRepo,
        sessionManager = sessionManager,
        address = Address.Empty
    )
}