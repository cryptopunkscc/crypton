@file:Suppress("FunctionName")

package cc.cryptopunks.crypton.presentation

import cc.cryptopunks.crypton.actor.Actor
import cc.cryptopunks.crypton.model.Model
import cc.cryptopunks.crypton.presenter.Presenter
import kotlinx.coroutines.cancel
import kotlinx.coroutines.cancelChildren

class Presentation<in A, in P : Presenter<A>> {

    val actorScope = Actor.Scope()
    val modelScope = Model.Scope()

    private var actor: A? = null
    private var presenter: P? = null

    fun setActor(actor: A?): Unit = synchronized(this) {
        actorScope.coroutineContext.cancelChildren()
        this.actor = actorScope(actor, presenter)
    }

    fun setPresenter(presenter: P?): Unit = synchronized(this) {
        modelScope.coroutineContext.cancelChildren()
        actorScope.coroutineContext.cancelChildren()
        this.presenter = modelScope(presenter)
        actorScope(actor, presenter)
    }

    fun clearActor() = setActor(null)

    fun clearPresenter() = setPresenter(null)

    fun cancel() {
        modelScope.cancel()
        actorScope.cancel()
    }

    fun Snapshot() = Snapshot(
        actor = actor,
        presenter = presenter
    )

    class Snapshot internal constructor(
        val actor: Any?,
        val presenter: Presenter<*>?
    ) {
        val isVisible get() = actor != null
        val isAttached get() = presenter != null
        inline fun <reified T : Any> isTypeOf() = presenter is T
    }
}