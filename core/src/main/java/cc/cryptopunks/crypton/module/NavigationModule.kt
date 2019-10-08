package cc.cryptopunks.crypton.module

import cc.cryptopunks.crypton.component.CoreComponent
import cc.cryptopunks.crypton.component.NavigationComponent
import cc.cryptopunks.crypton.util.Navigate
import cc.cryptopunks.crypton.util.OptionItemSelected

class NavigationModule(
    private val coreComponent: CoreComponent,
    private val optionItemSelectedComponent: OptionItemSelected.Component = OptionItemSelected.Module(),
    private val navigateComponent: Navigate.Component = Navigate.Module()
) : NavigationComponent,
    CoreComponent by coreComponent,
    OptionItemSelected.Component by optionItemSelectedComponent,
    Navigate.Component by navigateComponent