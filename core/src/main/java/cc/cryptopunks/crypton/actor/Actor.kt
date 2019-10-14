package cc.cryptopunks.crypton.actor

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope

interface Actor {
    class Scope : Actor, CoroutineScope by MainScope()
}