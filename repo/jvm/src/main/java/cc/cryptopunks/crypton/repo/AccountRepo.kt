package cc.cryptopunks.crypton.repo

import cc.cryptopunks.crypton.context.Account
import cc.cryptopunks.crypton.context.Address
import cc.cryptopunks.crypton.context.Password
import cc.cryptopunks.crypton.util.Store
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class AccountRepo : Account.Repo {

    private val store = Store(
        listOf(
            "test@test.io" to "test"
        ).map { (addr, pass) ->
            Address.from(addr).let { it to Account(it, Password(pass)) }
        }.toMap().filter { false }
    )

    override suspend fun contains(address: Address): Boolean = address in store.get()

    override fun get(address: Address): Account = store.get()[address]!!

    override suspend fun insert(account: Account): Account {
        store reduce { plus(account.address to account) }
        return account
    }

    override suspend fun update(account: Account) {
        insert(account)
    }

    override suspend fun delete(address: Address) {
        store reduce { minus(address) }
    }

    override suspend fun list(): List<Account> = store.get().values.toList()

    override fun flowList(): Flow<List<Address>> = store.changesFlow().map { it.keys.toList() }

    override suspend fun addressList(): List<Address> = store.get().keys.toList()
}
