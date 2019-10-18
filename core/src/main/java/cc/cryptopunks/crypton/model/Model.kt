package cc.cryptopunks.crypton.model

import cc.cryptopunks.crypton.presenter.Presenter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

object Model {
    class Scope : CoroutineScope by MainScope() {
        operator fun <P : Presenter<*>> invoke(
            presenter: P?
        ): P? = presenter?.also { init ->
            (this as CoroutineScope).launch {
                init()
            }
        }
    }
}