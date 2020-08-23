package cc.cryptopunks.crypton.emitter

import cc.cryptopunks.crypton.context.Exec
import cc.cryptopunks.crypton.context.SessionScope
import cc.cryptopunks.crypton.selector.presenceChangedFlow
import kotlinx.coroutines.flow.map

internal fun SessionScope.handlePresenceFlow() =
    presenceChangedFlow().map { Exec.HandlePresence(it.presence) }
