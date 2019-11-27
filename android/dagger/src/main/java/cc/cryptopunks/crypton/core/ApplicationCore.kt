package cc.cryptopunks.crypton.core

import cc.cryptopunks.crypton.FeatureManager
import cc.cryptopunks.crypton.ServiceCore
import cc.cryptopunks.crypton.annotation.ApplicationScope
import cc.cryptopunks.crypton.service.AppServices
import dagger.Component

@ApplicationScope
@Component(
    dependencies = [
        AndroidCore::class
    ],
    modules = [
        CreateAndroidFeatureCore.Bindings::class,
        AndroidServiceCoreFactory.Binding::class
    ]
)
interface ApplicationCore :
    AndroidCore,
    AppServices,
    FeatureManager.Core,
    ServiceCore.Factory.Core