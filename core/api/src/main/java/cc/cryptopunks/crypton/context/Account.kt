package cc.cryptopunks.crypton.context

import cc.cryptopunks.crypton.Scoped
import kotlinx.coroutines.flow.Flow

data class Account(
    val address: Address = Address.Empty,
    val password: Password = Password.Empty,
    val enabled: Boolean = true
) {
    val domain get() = address.domain

    interface Action : Scoped<SessionScope>

    companion object {
        val Empty = Account()
    }

    class Exception(
        val account: Address,
        message: String
    ) : kotlin.Exception(
        "$account $message"
    )

    interface Event : Api.Event

    data class Authenticated(val resumed: Boolean) : Event

    interface Service {
        data class Connecting(val account: Address) : Status
        data class Connected(val account: Address) : Status
        data class Error(val account: Address, val message: String? = null) : Status
        data class Accounts(val list: List<Address>)
        data class HasAccounts(val condition: Boolean)

        interface Status
    }

    interface Net {
        fun createAccount()
        fun removeAccount()
        fun login()
        fun isAuthenticated(): Boolean
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

    enum class Field {
        ServiceName,
        UserName,
        Password,
        ConfirmPassword
    }
}
