package cc.cryptopunks.crypton.view

import android.content.Context
import android.view.View
import android.widget.FrameLayout
import cc.cryptopunks.crypton.account.R
import cc.cryptopunks.crypton.context.Actor
import cc.cryptopunks.crypton.context.Service
import cc.cryptopunks.crypton.util.bindings.clicks
import cc.cryptopunks.crypton.util.ext.map
import cc.cryptopunks.crypton.viewmodel.SetAccountService.Input.AddAccount
import cc.cryptopunks.crypton.viewmodel.SetAccountService.Input.RegisterAccount
import kotlinx.android.synthetic.main.set_account.view.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class SetAccountView(
    context: Context
) :
    FrameLayout(context),
    Service.Wrapper {

    private val scope = Actor.Scope()

    override val wrapper = wrapper(Actor.Scope())

    init {
        View.inflate(context, R.layout.set_account, this)
        scope.run {
            launch { addButton.clicks().map { AddAccount }.collect(out) }
            launch { registerButton.clicks().map { RegisterAccount }.collect(out) }
        }
    }
}