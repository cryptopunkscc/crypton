package cc.cryptopunks.crypton.module

import android.app.Service
import cc.cryptopunks.crypton.BaseApplication
import cc.cryptopunks.crypton.app
import cc.cryptopunks.crypton.component.FeatureComponent
import cc.cryptopunks.crypton.util.*

class FeatureModule(
    private val applicationComponent: BaseApplication.Component,
    private val optionItemSelectedComponent: OptionItemSelected.Component = OptionItemSelected.Module(),
    private val navigateComponent: Navigate.Component = Navigate.Module()
) : FeatureComponent,
    BaseApplication.Component by applicationComponent,
    OptionItemSelected.Component by optionItemSelectedComponent,
    Navigate.Component by navigateComponent {

    override val useCaseScope = Scopes.UseCase(broadcastError)
}

fun Service.featureComponent() = FeatureModule(
    applicationComponent = app.component
)