package cc.cryptopunks.crypton.selector

import cc.cryptopunks.crypton.context.Exec
import cc.cryptopunks.crypton.context.SessionScope
import kotlinx.coroutines.flow.map

internal fun SessionScope.handlePresenceFlow() =
    presenceChangedFlow().map { Exec.HandlePresence(it.presence) }
