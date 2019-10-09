package cc.cryptopunks.crypton.entity

import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow

data class Account constructor(
    val address: Address = Address.Empty,
    val status: Status = Status.Disconnected,
    val password: String = "",
    val updateAt: Long = System.currentTimeMillis(),
    val current: Boolean = true
) {

    val domain get() = address.domain

    enum class Status {
        Disconnected,
        RequestConnect,
        RequestDisconnect,
        Connecting,
        Connected,
        Error;
    }

    data class Exception(
        val account: Account,
        override val cause: Throwable
    ) : kotlin.Exception(

        account.toString(),
        cause
    )

    @Suppress("NOTHING_TO_INLINE")
    inline fun exception(throwable: Throwable): Exception =
        if (throwable is Exception && throwable.account == this) throwable
        else Exception(this, throwable)

    interface Repo {
        fun contains(address: Address): Boolean
        fun get(address: Address): Account
        fun insert(account: Account): Account
        fun update(account: Account)
        fun delete(account: Account)
        suspend fun list(): List<Account>
        fun flowList(): Flow<List<Account>>
    }

    companion object {
        val Empty = Account()
    }
}

infix fun Job.onAccountException(handle: (Account) -> Any) = apply {
    invokeOnCompletion { throwable ->
        if (throwable is Account.Exception)
            handle(throwable.account)
    }
}