package cc.cryptopunks.crypton

import cc.cryptopunks.crypton.core.Core
import cc.cryptopunks.crypton.entity.Chat
import cc.cryptopunks.crypton.entity.Session
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