package cc.cryptopunks.crypton.component

import cc.cryptopunks.crypton.actor.OptionItemSelected
import cc.cryptopunks.crypton.navigation.Navigate

interface NavigationComponent :
    OptionItemSelected.Component,
    Navigate.Component