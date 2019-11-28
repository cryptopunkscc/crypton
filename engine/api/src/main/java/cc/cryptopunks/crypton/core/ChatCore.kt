package cc.cryptopunks.crypton.core

import cc.cryptopunks.crypton.context.Api
import cc.cryptopunks.crypton.context.Chat
import cc.cryptopunks.crypton.context.Feature
import cc.cryptopunks.crypton.context.Session

interface ChatCore :
    Api.Core,
    Feature.Core,
    Session.Core {

    val chat: Chat

    interface Factory : (Chat) -> ChatCore
}