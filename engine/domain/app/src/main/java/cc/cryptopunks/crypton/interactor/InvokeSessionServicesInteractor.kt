package cc.cryptopunks.crypton.interactor

import cc.cryptopunks.crypton.context.AppCore
import cc.cryptopunks.crypton.context.Session

class InvokeSessionServicesInteractor(
    private val appCore: AppCore
) {
    suspend operator fun invoke(session: Session) = appCore
        .featureCore()
        .sessionCore(session)
        .sessionBackgroundServices
        .forEach { service ->
            session.scope.launch {
                service()
            }
        }
}
