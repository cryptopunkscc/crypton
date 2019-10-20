package cc.cryptopunks.crypton.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import cc.cryptopunks.crypton.coreComponent
import cc.cryptopunks.crypton.entity.Chat
import cc.cryptopunks.crypton.entity.Message
import cc.cryptopunks.crypton.presentation.PresentationComponent
import cc.cryptopunks.crypton.presenter.DaggerRosterPresenter_Component
import cc.cryptopunks.crypton.presenter.RosterPresenter
import cc.cryptopunks.crypton.repo.repo
import cc.cryptopunks.crypton.view.RosterView

class RosterFragment : PresenterFragment<
        RosterPresenter.View,
        RosterPresenter,
        RosterPresenter.Component>() {

    override suspend fun onCreateComponent(
        component: PresentationComponent
    ) = DaggerRosterPresenter_Component
        .builder()
        .executorsComponent(coreComponent)
        .component(navigationComponent)
        .repo(coreComponent.repo<Chat.Repo>())
        .repo(coreComponent.repo<Message.Repo>())
        .build()!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = RosterView(context!!)
}