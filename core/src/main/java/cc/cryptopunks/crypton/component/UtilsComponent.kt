package cc.cryptopunks.crypton.component

import cc.cryptopunks.crypton.util.ActivityStack
import cc.cryptopunks.crypton.util.BroadcastError

interface UtilsComponent : BroadcastError.Component {
    val activityStackCache: ActivityStack.Cache
}