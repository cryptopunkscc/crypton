package cc.cryptopunks.crypton.component

import androidx.fragment.app.FragmentActivity
import cc.cryptopunks.crypton.module.FeatureModule
import cc.cryptopunks.crypton.module.FeatureScope
import cc.cryptopunks.crypton.util.DependenciesFragment
import cc.cryptopunks.crypton.util.Navigate
import cc.cryptopunks.crypton.util.OptionItemSelected
import cc.cryptopunks.crypton.util.ext.fragment
import dagger.Component

@FeatureScope
@Component(
    dependencies = [ApplicationComponent::class],
    modules = [
        FeatureModule::class,
        OptionItemSelected.Module::class,
        Navigate.Bindings::class
    ]
)
interface FeatureComponent :
    ApplicationComponent,
    OptionItemSelected.Component,
    Navigate.Component


fun FragmentActivity.featureComponent(
    applicationComponent: ApplicationComponent
): FeatureComponent = fragment("feature") {
    DependenciesFragment<FeatureComponent>()
}.init {
    createFeatureComponent(applicationComponent)
}

fun createFeatureComponent(
    applicationComponent: ApplicationComponent
): FeatureComponent = DaggerFeatureComponent
    .builder()
    .applicationComponent(applicationComponent)
    .featureModule(FeatureModule())
    .build()