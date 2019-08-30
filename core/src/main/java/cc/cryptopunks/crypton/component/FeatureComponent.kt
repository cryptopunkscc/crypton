package cc.cryptopunks.crypton.component

import cc.cryptopunks.crypton.module.FeatureModule
import cc.cryptopunks.crypton.module.FeatureScope
import cc.cryptopunks.crypton.util.Navigate
import cc.cryptopunks.crypton.util.OptionItemSelected
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