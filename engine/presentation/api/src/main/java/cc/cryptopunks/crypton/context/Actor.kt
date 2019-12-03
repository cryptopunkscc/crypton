package cc.cryptopunks.crypton.context

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

interface Actor {
    class Scope : CoroutineScope by MainScope() {
        operator fun <A, P : Presenter<A>> invoke(actor: A?, presenter: P?) =
            actor?.also { view -> presenter?.run { launch { view() } } }
    }
}