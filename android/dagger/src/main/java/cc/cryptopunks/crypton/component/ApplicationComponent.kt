package cc.cryptopunks.crypton.component

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
        ServiceComponentFactory.Binding::class
    ]
)
interface ApplicationComponent :
    AndroidCore,
    AppServices,
    FeatureManager.Component,
    ServiceCore.Factory.Component