package cc.cryptopunks.crypton.context

import kotlinx.coroutines.flow.Flow

data class Account(
    val address: Address = Address.Empty,
    val password: CharSequence = ""
) {
    val domain get() = address.domain

    companion object {
        val Empty = Account()

        @Suppress("FunctionName")
        fun Exception(
            account: Address,
            cause: Throwable
        ): Exception = if (cause is Exception) cause else Exception(account, cause)
    }

    class Exception private constructor(
        val account: Address,
        override val cause: Throwable
    ) : kotlin.Exception(
        account.toString(),
        cause
    )

    interface Event : Api.Event

    data class Authenticated(val resumed: Boolean) : Event

    interface Net {

        val createAccount: Create
        val removeAccount: Remove
        val login: Login
        val isAuthenticated: IsAuthenticated

        interface Create : () -> Unit
        interface Remove : () -> Unit
        interface Login : () -> Unit
        interface IsAuthenticated : () -> Boolean
    }

    interface Repo {
        suspend fun contains(address: Address): Boolean
        fun get(address: Address): Account
        suspend fun insert(account: Account): Account
        suspend fun update(account: Account)
        suspend fun delete(address: Address)
        suspend fun list(): List<Account>
        fun flowList(): Flow<List<Address>>
        suspend fun addressList(): List<Address>
    }

    interface Core {
        val accountRepo: Repo
        val accountNet: Net
    }
}
