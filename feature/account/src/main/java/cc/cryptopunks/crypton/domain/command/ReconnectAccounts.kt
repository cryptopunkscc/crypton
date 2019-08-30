package cc.cryptopunks.crypton.domain.command

import cc.cryptopunks.crypton.entity.Account
import cc.cryptopunks.crypton.domain.repository.AccountRepository
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class ReconnectAccounts @Inject constructor(
    private val dao: Account.Dao,
    private val repository: AccountRepository,
    private val connect: ConnectAccount
) : () -> Completable {

    override fun invoke() = Observable
        .defer { Observable.fromIterable(dao.getAll()) }
        .map { repository.copy().invoke(it) }
        .filter { it.shouldReconnect() }
        .observeOn(Schedulers.newThread())
        .map { it.id }
        .flatMapCompletable(connect)!!

    private fun AccountRepository.shouldReconnect() = listOf(
        Account.Status.Connecting,
        Account.Status.Connected
    ).contains(get().status) && !isInitialized
}