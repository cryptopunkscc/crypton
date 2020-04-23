package cc.cryptopunks.crypton.view

import android.content.Context
import android.view.View
import cc.cryptopunks.crypton.account.R
import cc.cryptopunks.crypton.context.Connector
import cc.cryptopunks.crypton.context.Route
import cc.cryptopunks.crypton.util.bindings.clicks
import cc.cryptopunks.crypton.util.ext.map
import cc.cryptopunks.crypton.widget.ActorLayout
import kotlinx.android.synthetic.main.set_account.view.*
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flattenMerge
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch

class SetAccountView(
    context: Context
) :
    ActorLayout(context) {

    init {
        View.inflate(context, R.layout.set_account, this)
    }

    override fun Connector.connect(): Job = launch {
        flowOf(
            addButton.clicks().map { Route.Login },
            registerButton.clicks().map { Route.Register }
        )
            .flattenMerge()
            .collect(output)
    }
}