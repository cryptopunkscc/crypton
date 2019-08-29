package cc.cryptopunks.crypton.core.component

import cc.cryptopunks.crypton.core.module.FeatureModule
import cc.cryptopunks.crypton.core.module.FeatureScope
import cc.cryptopunks.crypton.core.util.Navigate
import cc.cryptopunks.crypton.core.util.OptionItemSelected
import dagger.Component

@FeatureScope
@Component(
    dependencies = [ApplicationComponent::class],
    modules = [
        FeatureModule::class,
        OptionItemSelected.Bindings::class,
        Navigate.Bindings::class
    ]
)
interface FeatureComponent :
    ApplicationComponent,
    OptionItemSelected.Component,
    Navigate.Component