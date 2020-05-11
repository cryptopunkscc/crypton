package cc.cryptopunks.crypton.interactor

import cc.cryptopunks.crypton.context.AppCore
import cc.cryptopunks.crypton.context.Session
import cc.cryptopunks.crypton.util.typedLog
import kotlinx.coroutines.launch

class InvokeSessionServicesInteractor(
    private val appCore: AppCore
) {
    private val log = typedLog()
    suspend operator fun invoke(session: Session) = appCore
        .also { log.d("Invoke session services for: ${session.address}") }
        .sessionCore(session)
        .sessionBackgroundServices
        .forEach { service ->
            session.scope.launch {
                service()
            }
        }
}
