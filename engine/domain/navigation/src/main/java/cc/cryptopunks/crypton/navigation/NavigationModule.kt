package cc.cryptopunks.crypton.navigation

class NavigationModule(
    private val optionItemSelectedComponent: OptionItemSelected.Component = OptionItemSelected.Module(),
    private val navigateComponent: Navigate.Component = Navigate.Module()
) : Navigation,
    OptionItemSelected.Component by optionItemSelectedComponent,
    Navigate.Component by navigateComponent