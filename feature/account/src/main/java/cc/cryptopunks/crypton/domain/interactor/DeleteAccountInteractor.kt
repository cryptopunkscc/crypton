package cc.cryptopunks.crypton.domain.interactor

import cc.cryptopunks.crypton.domain.repository.AccountRepository
import cc.cryptopunks.crypton.util.Scopes
import kotlinx.coroutines.Job
import javax.inject.Inject

class DeleteAccountInteractor @Inject constructor(
    repository: AccountRepository,
    scope: Scopes.Feature
) : (Long) -> Job by { id ->
    scope.launch {
        repository.copy().run {
            load(id)
            delete()
        }
    }
}