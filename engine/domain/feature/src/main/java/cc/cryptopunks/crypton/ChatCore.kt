package cc.cryptopunks.crypton

import cc.cryptopunks.crypton.context.*

interface ChatCore :
    Api.Core,
    Feature.Core,
    Session.Core {

    val chat: Chat

    interface Factory : (Chat) -> ChatCore
}