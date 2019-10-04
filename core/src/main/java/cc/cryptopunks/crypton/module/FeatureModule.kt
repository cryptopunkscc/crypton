package cc.cryptopunks.crypton.module

import cc.cryptopunks.crypton.BaseApplication
import cc.cryptopunks.crypton.component.FeatureComponent
import cc.cryptopunks.crypton.component.ViewModelComponent
import cc.cryptopunks.crypton.util.Navigate
import cc.cryptopunks.crypton.util.OptionItemSelected
import cc.cryptopunks.crypton.util.Scopes

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

class ViewModelModule(
    appComponent: BaseApplication.Component
) : ViewModelComponent,
    BaseApplication.Component by appComponent {
    override val viewModelScope = Scopes.ViewModel(broadcastError)
}