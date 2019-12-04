package cc.cryptopunks.crypton.view

import android.view.View
import cc.cryptopunks.crypton.context.Actor
import cc.cryptopunks.crypton.context.OptionItem
import cc.cryptopunks.crypton.context.Service
import cc.cryptopunks.crypton.presenter.DashboardService
import cc.cryptopunks.crypton.util.bindings.clicks
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.dashboard.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class DashboardView(
    private val optionItems: OptionItem.Output,
    override val containerView: View
) :
    Service.Wrapper,
    LayoutContainer {

    private val scope = Actor.Scope()

    override val wrapper = wrapper(scope)

    init {
        scope.run {
            launch { optionItems.collect { DashboardService.Input.ManageAccounts.out() } }
            launch { createConversationButton.clicks().collect { DashboardService.Input.CreateChat.out() } }
        }
    }
}