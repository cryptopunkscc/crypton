package cc.cryptopunks.crypton.context

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

interface Presenter<in A> {
    suspend operator fun invoke(): Any = Unit
    suspend operator fun A.invoke(): Any = Unit

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