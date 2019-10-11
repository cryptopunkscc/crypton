package cc.cryptopunks.crypton.model

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope

object Model {
    class Scope : CoroutineScope by MainScope()
}