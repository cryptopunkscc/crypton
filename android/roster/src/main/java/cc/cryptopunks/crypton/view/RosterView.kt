package cc.cryptopunks.crypton.view

import android.content.Context
import android.view.View
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import cc.cryptopunks.crypton.adapter.RosterAdapter
import cc.cryptopunks.crypton.chat.R
import cc.cryptopunks.crypton.context.Connector
import cc.cryptopunks.crypton.context.Roster
import cc.cryptopunks.crypton.navigate.currentAccount
import cc.cryptopunks.crypton.navigate.navigateChat
import cc.cryptopunks.crypton.util.typedLog
import cc.cryptopunks.crypton.widget.ActorLayout
import kotlinx.android.synthetic.main.roster.view.*
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.launch

class RosterView(
    context: Context
) : ActorLayout(context) {

    private val rosterAdapter = RosterAdapter(coroutineContext)

    private val rosterItemDecorator = RosterItemDecorator(
        context = context,
        paddingLeft = resources.getDimensionPixelSize(R.dimen.roster_divider_padding_left),
        paddingRight = resources.getDimensionPixelSize(R.dimen.roster_divider_padding_right)
    )

    private val log = typedLog()

    init {
        log.d("init")
        View.inflate(context, R.layout.roster, this)
        rosterRecyclerView.apply {
            addItemDecoration(rosterItemDecorator)
            layoutManager = LinearLayoutManager(context)
            adapter = rosterAdapter
        }
    }

    override fun Connector.connect(): Job = launch {
        log.d("Connect")
        launch {
            input.filterIsInstance<Roster.Service.Items>().collect {
                log.d("Received ${it.list.size} items")
                rosterAdapter.submitList(it.list)
            }
        }
        launch {
            Roster.Service.GetItems.out()
            Roster.Service.SubscribeItems(true).out()
        }
        launch {
            rosterAdapter.clicks.asFlow().collect {
                findNavController().navigateChat(
                    account = context.currentAccount,
                    chat = it.item.chatAddress
                )
            }
        }
    }

    override fun onDetachedFromWindow() {
        coroutineContext.cancelChildren()
        super.onDetachedFromWindow()
    }
}
