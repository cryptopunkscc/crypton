package cc.cryptopunks.crypton.context

import kotlinx.coroutines.flow.Flow

data class Account(
    val address: Address = Address.Empty,
    val password: CharSequence = ""
) {

    val domain get() = address.domain

    data class Exception(
        val account: Address,
        override val cause: Throwable
    ) : kotlin.Exception(
        account.toString(),
        cause
    )

    @Suppress("NOTHING_TO_INLINE")
    inline fun exception(throwable: Throwable): Exception =
        if (throwable is Exception) throwable
        else Exception(address, throwable)

    interface Event : Api.Event {

        data class Authenticated(
            val resumed: Boolean
        ) : Event
    }

    interface Net {

        val createAccount: Create
        val removeAccount: Remove
        val login: Login
        val isAuthenticated: IsAuthenticated

        interface Create: () -> Unit
        interface Remove: () -> Unit
        interface Login: () -> Unit
        interface IsAuthenticated: () -> Boolean
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

    companion object {
        val Empty = Account()
    }
}