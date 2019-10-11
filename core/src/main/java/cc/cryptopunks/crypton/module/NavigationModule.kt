package cc.cryptopunks.crypton.module

import cc.cryptopunks.crypton.actor.OptionItemSelected
import cc.cryptopunks.crypton.component.NavigationComponent
import cc.cryptopunks.crypton.navigation.Navigate

class NavigationModule(
    private val optionItemSelectedComponent: OptionItemSelected.Component = OptionItemSelected.Module(),
    private val navigateComponent: Navigate.Component = Navigate.Module()
) : NavigationComponent,
    OptionItemSelected.Component by optionItemSelectedComponent,
    Navigate.Component by navigateComponent