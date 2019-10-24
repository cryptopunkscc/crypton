package cc.cryptopunks.crypton.module

import cc.cryptopunks.crypton.component.AndroidCore
import cc.cryptopunks.crypton.core.Core
import cc.cryptopunks.crypton.entity.Session
import cc.cryptopunks.crypton.factory.SessionServicesFactory
import cc.cryptopunks.crypton.interactor.StartSessionServices
import cc.cryptopunks.crypton.session.SessionServices
import dagger.Binds
import dagger.Module

@Module
interface AppBindings {

    @Binds
    fun AndroidCore.core(): Core

    @Binds
    fun StartSessionServices.startSessionServices(): Session.StartServices

    @Binds
    fun SessionServicesFactory.sessionServicesFactory(): SessionServices.Factory
}