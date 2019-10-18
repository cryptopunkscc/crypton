package cc.cryptopunks.crypton.fragment

import android.os.Bundle
import android.view.View
import cc.cryptopunks.crypton.actor.Actor
import cc.cryptopunks.crypton.applicationComponent
import cc.cryptopunks.crypton.component.PresentationComponent
import cc.cryptopunks.crypton.presenter.Presenter
import cc.cryptopunks.crypton.util.connectedCache
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch


abstract class PresenterFragment<
        A : Actor,
        P : Presenter<A>,
        C : Presenter.Component<P>> :
    CoreFragment() {

    abstract suspend fun onCreateComponent(component: PresentationComponent): C

    private val presentationManager get() = applicationComponent.presentationManager

    val presentation by lazy { presentationManager.create<A, P>() }

    private val component: Flow<C> by lazy {
        presentationComponentFlow
            .map { component -> onCreateComponent(component) }
            .connectedCache(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        presentation
        launch { component.filterNotNull().collect { presentation.setPresenter(it.presenter) } }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        presentation.setActor(onCreateActor(view))
    }

    open fun onCreateActor(view: View): A = view as A

    override fun onDestroyView() {
        super.onDestroyView()
        presentation.clearActor()
    }

    override fun onDestroy() {
        super.onDestroy()
        presentationManager.remove(presentation)
        presentation.run {
            clearPresenter()
            cancel()
        }
    }
}
