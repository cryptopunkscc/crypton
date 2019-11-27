package cc.cryptopunks.crypton

import cc.cryptopunks.crypton.context.Chat
import cc.cryptopunks.crypton.context.Core
import cc.cryptopunks.crypton.context.Session
import cc.cryptopunks.crypton.navigation.OptionItem
import cc.cryptopunks.crypton.navigation.Route

interface ChatCore :
    Core,
    Route.Api,
    OptionItem.Api {

    val session: Session
    val chat: Chat

    interface Factory : (Chat) -> ChatCore
}