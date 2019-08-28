package cc.cryptopunks.crypton.core.component

import cc.cryptopunks.crypton.core.util.OptionItemSelectedBroadcast
import cc.cryptopunks.crypton.core.module.FeatureModule
import cc.cryptopunks.crypton.core.module.FeatureScope
import cc.cryptopunks.crypton.core.util.Navigation
import dagger.Component

@FeatureScope
@Component(
    dependencies = [ApplicationComponent::class],
    modules = [FeatureModule::class]
)
interface FeatureComponent : ApplicationComponent {
    val navigation: Navigation
    val navigationBus: Navigation.Bus
    val broadcastOptionItemSelected: OptionItemSelectedBroadcast
}