package cc.cryptopunks.crypton.presenter

import cc.cryptopunks.crypton.actor.Actor
import cc.cryptopunks.crypton.model.Model
import kotlinx.coroutines.cancel
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.launch

class ActorPresenterManager<A : Actor, P : Presenter<A>> {

    val actorScope = Actor.Scope()
    val modelScope = Model.Scope()

    private var actor: A? = null
    private var presenter: P? = null

    fun setActor(actor: A?) = synchronized(this) {
        actorScope.coroutineContext.cancelChildren()
        this.actor = actor
        bind()
    }

    fun setPresenter(presenter: P?) = synchronized(this) {
        modelScope.coroutineContext.cancelChildren()
        actorScope.coroutineContext.cancelChildren()
        this.presenter = presenter?.apply {
            modelScope.launch {
                invoke()
            }
        }
        bind()
    }

    private fun bind() {
        actor?.let { view ->
            presenter?.run {
                actorScope.launch {
                    view.invoke()
                }
            }
        }
    }

    fun clearActor() = setActor(null)

    fun clearPresenter() = setPresenter(null)

    fun cancel() {
        modelScope.cancel()
        actorScope.cancel()
    }
}