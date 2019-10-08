package cc.cryptopunks.crypton.component

import cc.cryptopunks.crypton.util.Navigate
import cc.cryptopunks.crypton.util.OptionItemSelected

interface NavigationComponent :
    CoreComponent,
    OptionItemSelected.Component,
    Navigate.Component