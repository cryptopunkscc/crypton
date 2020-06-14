package cc.cryptopunks.crypton.context

import kotlinx.coroutines.flow.Flow

data class Account(
    val address: Address = Address.Empty,
    val password: Password = Password.Empty
) {
    val domain get() = address.domain

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

    interface Service {

        data class Set(val field: Field, val text: CharSequence)
        object Register
        object Login

        interface Status {
            val address: Address
        }

        data class Connecting(override val address: Address) : Status
        data class Connected(override val address: Address) : Status
        data class Error(override val address: Address, val message: String? = null) : Status
    }

    enum class Field {
        ServiceName,
        UserName,
        Password,
        ConfirmPassword
    }
}
