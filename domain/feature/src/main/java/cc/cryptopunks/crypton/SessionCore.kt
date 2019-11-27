package cc.cryptopunks.crypton

import cc.cryptopunks.crypton.core.Core
import cc.cryptopunks.crypton.entity.Session
import cc.cryptopunks.crypton.navigation.Navigation

interface SessionCore :
    Core,
    Navigation {
    val session: Session
    val chatFeature: ChatFeatureCore.Factory
}