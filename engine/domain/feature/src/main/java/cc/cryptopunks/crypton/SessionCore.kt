package cc.cryptopunks.crypton

import cc.cryptopunks.crypton.context.Core
import cc.cryptopunks.crypton.context.Session
import cc.cryptopunks.crypton.context.OptionItem
import cc.cryptopunks.crypton.context.Route

interface SessionCore :
    Core,
    Route.Api,
    OptionItem.Api {

    val session: Session
    val chatFeature: ChatCore.Factory
}