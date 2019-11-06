package cc.cryptopunks.crypton.component

import cc.cryptopunks.crypton.FeatureManager
import cc.cryptopunks.crypton.ServiceCore
import cc.cryptopunks.crypton.annotation.ApplicationScope
import dagger.Component

@ApplicationScope
@Component(
    dependencies = [
        AndroidCore::class
    ],
    modules = [
        FeatureComponent.Bindings::class,
        ServiceComponentFactory.Binding::class
    ]
)
interface ApplicationComponent :
    AndroidCore,
    FeatureManager.Component,
    ServiceCore.Factory.Component