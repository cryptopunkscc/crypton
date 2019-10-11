package cc.cryptopunks.crypton.navigation

import cc.cryptopunks.crypton.actor.OptionItemSelected

object Navigation {

    interface Component :
        OptionItemSelected.Component,
        Navigate.Component
}