package cc.cryptopunks.crypton.fragment

import android.os.Bundle
import android.view.View
import cc.cryptopunks.crypton.context.Presenter
import cc.cryptopunks.crypton.presentation.PresentationManager
import cc.cryptopunks.crypton.util.ext.resolve

abstract class PresenterFragment<A, P : Presenter<A>> :
    FeatureFragment() {

    private val presentationManager by lazy {
        appCore.resolve<PresentationManager.Core>().presentationManager
    }

    val presentation by lazy {
        presentationManager.create<A, P>()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        presentation.setPresenter(onCreatePresenter())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        presentation.setActor(onCreateActor(view))
    }

    open fun onCreatePresenter(): P? = null

    open fun onCreateActor(view: View): A? = view as? A

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