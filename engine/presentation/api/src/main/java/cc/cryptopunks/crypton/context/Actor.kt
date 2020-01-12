package cc.cryptopunks.crypton.context

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

interface Actor {
    class Scope : CoroutineScope {

        override val coroutineContext = SupervisorJob() + Dispatchers.Main

        operator fun <A, P : Presenter<A>> invoke(actor: A?, presenter: P?) =
            actor?.also { view -> presenter?.run { launch { view() } } }
    }
}