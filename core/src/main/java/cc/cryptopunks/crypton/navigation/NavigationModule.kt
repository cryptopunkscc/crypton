package cc.cryptopunks.crypton.navigation

import cc.cryptopunks.crypton.actor.OptionItemSelected

class NavigationModule(
    private val optionItemSelectedComponent: OptionItemSelected.Component = OptionItemSelected.Module(),
    private val navigateComponent: Navigate.Component = Navigate.Module()
) : Navigation.Component,
    OptionItemSelected.Component by optionItemSelectedComponent,
    Navigate.Component by navigateComponent