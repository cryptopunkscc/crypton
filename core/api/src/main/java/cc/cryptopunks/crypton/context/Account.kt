package cc.cryptopunks.crypton.context

import cc.cryptopunks.crypton.Async
import cc.cryptopunks.crypton.Scoped
import cc.cryptopunks.crypton.Subscription
import kotlinx.coroutines.flow.Flow
import java.util.concurrent.CancellationException

private var removeCounter = 1

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

    data class Authenticated(val resumed: Boolean) : Action, Event, Async

    interface Service {

        // input

        interface Connect : Scoped<AppScope>
        data class Register(val account: Account? = null) : Connect
        data class Add(val account: Account? = null) : Connect

        object StartServices : Action, Async

        object GetAccountList : Main.Action
        data class SubscribeAccountList(override val enable: Boolean) :
            Main.Action,
            Subscription

        object Login : Action
        object Logout : Action, CancellationException()
        data class Enable(val condition: Boolean) : Action
        data class Remove(val deviceOnly: Boolean = true, val id: Int = removeCounter++) : Action
        // output

        data class Connecting(val account: Address) : Status
        data class Connected(val account: Address) : Status
        data class Error(val account: Address, val message: String? = null) : Status
        data class Accounts(val list: List<Address>)
        data class HasAccounts(val condition: Boolean) : Main.Action, Async

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
