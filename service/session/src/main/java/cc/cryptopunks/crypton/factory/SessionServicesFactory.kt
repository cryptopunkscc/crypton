package cc.cryptopunks.crypton.factory

import cc.cryptopunks.crypton.core.Core
import cc.cryptopunks.crypton.entity.Session
import cc.cryptopunks.crypton.service.SessionServices
import javax.inject.Inject
import javax.inject.Singleton

class SessionServicesFactory @Inject constructor(
    private val core: Core
) {

    @Singleton
    @dagger.Component(
        dependencies = [
            Session::class,
            Core::class
        ]
    )
    interface Component : SessionServices

    operator fun invoke(session: Session): SessionServices =
        DaggerSessionServicesFactory_Component
            .builder()
            .session(session)
            .core(core)
            .build()
}