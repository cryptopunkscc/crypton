package cc.cryptopunks.crypton.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import cc.cryptopunks.crypton.core
import cc.cryptopunks.crypton.core.Core
import cc.cryptopunks.crypton.navigation.Navigation
import cc.cryptopunks.crypton.presenter.Presenter
import cc.cryptopunks.crypton.presenter.RosterPresenter
import cc.cryptopunks.crypton.view.RosterView

class RosterFragment : PresenterFragment<RosterPresenter.View, RosterPresenter>() {

    @dagger.Component(
        dependencies = [
            Navigation::class,
            Core::class
        ]
    )
    interface Component : Presenter.Component<RosterPresenter>

    override fun onCreatePresenter(): RosterPresenter = DaggerRosterFragment_Component
        .builder()
        .core(core)
        .navigation(navigation)
        .build()
        .presenter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = RosterView(context!!)
}