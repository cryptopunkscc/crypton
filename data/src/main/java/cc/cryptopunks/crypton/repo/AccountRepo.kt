package cc.cryptopunks.crypton.repo

import cc.cryptopunks.crypton.entity.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

internal class AccountRepo(
    private val dao: AccountData.Dao
) : Account.Repo {

    override fun contains(address: Address): Boolean =
        dao.contains(address.id) != null

    override fun get(address: Address): Account =
        dao.get(address.id).toDomain()

    override fun insert(account: Account): Account =
        account.apply { dao.insert(chatData()) }

    override fun update(account: Account): Unit =
        dao.update(account.chatData())

    override fun delete(account: Account): Unit =
        dao.delete(account.chatData())

    override fun list(): List<Account> =
        dao.list().map { it.toDomain() }

    override fun flowList(): Flow<List<Account>> =
        dao.flowList().map { it.map(AccountData::toDomain) }
}