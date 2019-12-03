package cc.cryptopunks.crypton.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import cc.cryptopunks.crypton.presenter.RosterPresenter
import cc.cryptopunks.crypton.util.ext.resolve
import cc.cryptopunks.crypton.view.RosterView

class RosterFragment : PresenterFragment<RosterPresenter.Actor, RosterPresenter>() {

    override fun onCreatePresenter() = featureCore
        .resolve<RosterPresenter.Core>()
        .rosterPresenter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = RosterView(context!!)
}