package cc.cryptopunks.crypton.context

import cc.cryptopunks.crypton.Scoped
import cc.cryptopunks.crypton.util.OpenStore
import kotlinx.coroutines.flow.Flow

data class Account(
    val address: Address = Address.Empty,
    val password: Password = Password.Empty,
    val enabled: Boolean = true
) {
    val domain get() = address.domain

    data class Name(val address: Address) {
        override fun toString(): String = address.toString()
    }

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

    data class Connecting(val account: Address) : Status
    data class Connected(val account: Address) : Status
    data class Error(val account: Address, val message: String? = null) : Status
    data class Many(val accounts: Set<Address>)
    data class HasAccounts(val condition: Boolean)
    data class ChatCreated(val chat: Address)

    interface Status

    class Store : OpenStore<Many>(Many(emptySet()))

    interface Net {
        fun createAccount()
        fun removeAccount()
        fun login()
        fun isAuthenticated(): Boolean
    }

    interface Repo {
        suspend fun contains(address: Address): Boolean
        suspend fun get(address: Address): Account
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
