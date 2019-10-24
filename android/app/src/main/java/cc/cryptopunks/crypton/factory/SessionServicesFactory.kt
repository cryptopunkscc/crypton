package cc.cryptopunks.crypton.factory

import cc.cryptopunks.crypton.component.AndroidCore
import cc.cryptopunks.crypton.component.AppScope
import cc.cryptopunks.crypton.entity.Session
import cc.cryptopunks.crypton.session.SessionServices
import javax.inject.Inject

@AppScope
class SessionServicesFactory @Inject constructor(
    private val androidCore: AndroidCore
) : SessionServices.Factory {

    @AppScope
    @dagger.Component(
        dependencies = [
            Session::class,
            AndroidCore::class
        ]
    )
    interface Component : SessionServices

    override fun invoke(session: Session): SessionServices =
        DaggerSessionServicesFactory_Component
            .builder()
            .session(session)
            .androidCore(androidCore)
            .build()
}