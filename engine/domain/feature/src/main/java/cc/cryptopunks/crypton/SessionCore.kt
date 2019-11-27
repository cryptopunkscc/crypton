package cc.cryptopunks.crypton

import cc.cryptopunks.crypton.context.*

interface SessionCore :
    Api.Core,
    Feature.Core,
    Session.Core {

    val chatFeature: ChatCore.Factory
}