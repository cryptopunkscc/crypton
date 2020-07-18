package cc.cryptopunks.crypton.context

import cc.cryptopunks.crypton.Subscription
import kotlinx.coroutines.flow.Flow
import java.util.concurrent.CancellationException

data class Account(
    val address: Address = Address.Empty,
    val password: Password = Password.Empty,
    val enabled: Boolean = true
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

    interface Service {

        // input
        data class Set(val field: Field, val text: CharSequence)

        data class Register(val account: Account? = null) : Connect
        data class Add(val account: Account? = null) : Connect
        data class Login(val address: Address) : Connect
        data class Logout(val address: Address) : CancellationException()
        data class Remove(val address: Address, val deviceOnly: Boolean = true) : CancellationException()
        data class SubscribeAccountList(override val enable: Boolean) : Subscription
        object GetAccountList
        data class Enable(val address: Address, val condition: Boolean)

        interface Connect

        // output
        data class Connecting(override val address: Address) : Status
        data class Connected(override val address: Address) : Status
        data class Error(override val address: Address, val message: String? = null) : Status
        data class Accounts(val list: List<Address>)
        data class HasAccounts(val condition: Boolean)

        interface Status {
            val address: Address
        }
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
