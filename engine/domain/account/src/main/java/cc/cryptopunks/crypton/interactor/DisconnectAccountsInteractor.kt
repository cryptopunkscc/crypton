package cc.cryptopunks.crypton.interactor

import cc.cryptopunks.crypton.context.Account
import cc.cryptopunks.crypton.context.Service
import cc.cryptopunks.crypton.util.JobManager
import kotlinx.coroutines.Job
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DisconnectAccountsInteractor @Inject constructor(
    disconnectAccount: DisconnectAccountInteractor,
    private val scope: Service.Scope,
    private val repo: Account.Repo
) : () -> Job {

    private val disconnectIfNeeded = JobManager(
        scope = scope,
        execute = disconnectAccount
    )

    override fun invoke(): Job = scope.launch {
        repo.addressList().forEach { account ->
            disconnectIfNeeded.send(account)
        }
    }
}