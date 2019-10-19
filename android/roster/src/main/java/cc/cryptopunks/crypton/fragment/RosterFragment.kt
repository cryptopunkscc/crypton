package cc.cryptopunks.crypton.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import cc.cryptopunks.crypton.entity.Chat
import cc.cryptopunks.crypton.entity.Message
import cc.cryptopunks.crypton.navigation.Navigation
import cc.cryptopunks.crypton.presentation.PresentationComponent
import cc.cryptopunks.crypton.presenter.RosterPresenter
import cc.cryptopunks.crypton.presenter.Presenter
import cc.cryptopunks.crypton.util.ExecutorsComponent
import cc.cryptopunks.crypton.view.RosterView
import javax.inject.Singleton

class RosterFragment : PresenterFragment<
        RosterPresenter.View,
        RosterPresenter,
        RosterFragment.Component>() {

    @Singleton
    @dagger.Component(
        dependencies = [
            ExecutorsComponent::class,
            Navigation.Component::class,
            Chat.Repo::class,
            Message.Repo::class
        ]
    )
    interface Component : Presenter.Component<RosterPresenter>

    override suspend fun onCreateComponent(
        component: PresentationComponent
    ): Component = DaggerRosterFragment_Component
        .builder()
        .executorsComponent(component)
        .component(navigationComponent)
        .repo(component.chatRepo)
        .repo(component.messageRepo)
        .build()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = RosterView(context!!)
}