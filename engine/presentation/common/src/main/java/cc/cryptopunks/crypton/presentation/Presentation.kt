@file:Suppress("FunctionName")

package cc.cryptopunks.crypton.presentation

import cc.cryptopunks.crypton.context.Actor
import cc.cryptopunks.crypton.context.Presenter
import kotlinx.coroutines.cancel
import kotlinx.coroutines.cancelChildren

class Presentation<in A, in P : Presenter<A>> {

    val actorScope = Actor.Scope()
    val presenterScope = Presenter.Scope()

    private var actor: A? = null
    private var presenter: P? = null

    fun setActor(actor: A?): Unit = synchronized(this) {
        actorScope.coroutineContext.cancelChildren()
        this.actor = actorScope(actor, presenter)
    }

    fun setPresenter(presenter: P?): Unit = synchronized(this) {
        presenterScope.coroutineContext.cancelChildren()
        actorScope.coroutineContext.cancelChildren()
        this.presenter = presenterScope(presenter)
        actorScope(actor, presenter)
    }

    fun clearActor() = setActor(null)

    fun clearPresenter() = setPresenter(null)

    fun cancel() {
        presenterScope.cancel()
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