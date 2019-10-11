package cc.cryptopunks.crypton.actor

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope

object Actor {
    class Scope : CoroutineScope by MainScope()
}