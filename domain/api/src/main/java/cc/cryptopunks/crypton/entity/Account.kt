package cc.cryptopunks.crypton.entity

import cc.cryptopunks.crypton.api.Api
import kotlinx.coroutines.flow.Flow

data class Account(
    val address: Address = Address.Empty,
    val password: String = ""
) {

    val domain get() = address.domain

    data class Exception(
        val account: Account,
        override val cause: Throwable
    ) : kotlin.Exception(

        account.toString(),
        cause
    )

    @Suppress("NOTHING_TO_INLINE")
    inline fun exception(throwable: Throwable): Exception =
        if (throwable is Exception) throwable
        else Exception(this, throwable)

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
        suspend fun get(address: Address): Account
        suspend fun insert(account: Account): Account
        suspend fun update(account: Account)
        suspend fun delete(account: Account)
        suspend fun list(): List<Account>
        fun flowList(): Flow<List<Account>>
    }

    companion object {
        val Empty = Account()
    }
}