package cc.cryptopunks.crypton.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import cc.cryptopunks.crypton.presentation.PresentationComponent
import cc.cryptopunks.crypton.presenter.DaggerRosterPresenter_Component
import cc.cryptopunks.crypton.presenter.RosterPresenter
import cc.cryptopunks.crypton.view.RosterView

class RosterFragment : PresenterFragment<
        RosterPresenter.View,
        RosterPresenter,
        RosterPresenter.Component>() {

    override suspend fun onCreateComponent(
        component: PresentationComponent
    ) = DaggerRosterPresenter_Component
        .builder()
        .executorsComponent(component)
        .component(navigationComponent)
        .repo(component.chatRepo)
        .repo(component.messageRepo)
        .build()!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = RosterView(context!!)
}