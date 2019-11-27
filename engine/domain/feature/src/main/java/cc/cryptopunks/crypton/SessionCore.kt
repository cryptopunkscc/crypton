package cc.cryptopunks.crypton

import cc.cryptopunks.crypton.core.Core
import cc.cryptopunks.crypton.context.Session
import cc.cryptopunks.crypton.navigation.OptionItem
import cc.cryptopunks.crypton.navigation.Route

interface SessionCore :
    Core,
    Route.Api,
    OptionItem.Api {

    val session: Session
    val chatFeature: ChatCore.Factory
}