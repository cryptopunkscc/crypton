package cc.cryptopunks.crypton.interactor

import cc.cryptopunks.crypton.context.AppScope
import cc.cryptopunks.crypton.context.Session
import cc.cryptopunks.crypton.util.typedLog
import kotlinx.coroutines.launch

class InvokeSessionServicesInteractor(
    private val appScope: AppScope
) {
    private val log = typedLog()
    suspend operator fun invoke(session: Session) = appScope
        .also { log.d("Invoke session services for ${session.address}") }
        .sessionScope(session)
        .sessionBackgroundServices
        .forEach { service ->
            session.scope.launch {
                service()
            }
        }
}
