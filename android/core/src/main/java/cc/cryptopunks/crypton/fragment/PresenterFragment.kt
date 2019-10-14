package cc.cryptopunks.crypton.fragment

import android.os.Bundle
import android.view.View
import cc.cryptopunks.crypton.actor.Actor
import cc.cryptopunks.crypton.component.PresentationComponent
import cc.cryptopunks.crypton.presenter.Presenter
import cc.cryptopunks.crypton.presenter.ActorPresenterManager
import cc.cryptopunks.crypton.util.connectedCache
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch


abstract class
PresenterFragment<
        A : Actor,
        P : Presenter<A>,
        C : Presenter.Component<P>> :
    CoreFragment() {

    abstract suspend fun onCreateComponent(component: PresentationComponent): C

    val manager = ActorPresenterManager<A, P>()

    private val component: Flow<C> by lazy {
        presentationComponentFlow
            .map { component -> onCreateComponent(component) }
            .connectedCache(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        launch { component.filterNotNull().collect { manager.setPresenter(it.presenter) } }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        manager.setActor(onCreateActor(view))
    }

    open fun onCreateActor(view: View) : A = view as A

    override fun onDestroyView() {
        super.onDestroyView()
        manager.clearActor()
    }

    override fun onDestroy() {
        super.onDestroy()
        manager.run {
            clearPresenter()
            cancel()
        }
    }
}
