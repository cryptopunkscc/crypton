package cc.cryptopunks.crypton.account.domain.command

import cc.cryptopunks.crypton.account.domain.repository.AccountRepository
import cc.cryptopunks.crypton.account.util.wrap
import io.reactivex.Completable
import io.reactivex.Completable.fromAction
import javax.inject.Inject

class DeleteAccount @Inject constructor(
    repository: AccountRepository
) : (Long) -> Completable by { id ->
    repository.copy().run {
        fromAction {
            load(id)
            delete()
        }.onErrorResumeNext {
            Completable.error(wrap(it))
        }
    }
}