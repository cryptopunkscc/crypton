package cc.cryptopunks.crypton.core

import cc.cryptopunks.crypton.context.Api
import cc.cryptopunks.crypton.context.Feature
import cc.cryptopunks.crypton.context.Session

interface SessionCore :
    Api.Core,
    Feature.Core,
    Session.Core {

    val chatFeature: ChatCore.Factory
}