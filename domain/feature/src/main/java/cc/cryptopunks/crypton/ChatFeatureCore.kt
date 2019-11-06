package cc.cryptopunks.crypton

import cc.cryptopunks.crypton.core.Core
import cc.cryptopunks.crypton.entity.Chat
import cc.cryptopunks.crypton.entity.Session
import cc.cryptopunks.crypton.navigation.Navigation

interface ChatFeatureCore : Core,
    Navigation {
    val session: Session
    val chat: Chat
    interface Factory : (Chat) -> ChatFeatureCore
}