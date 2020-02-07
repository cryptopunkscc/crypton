package cc.cryptopunks.crypton.view

import android.content.Context
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import cc.cryptopunks.crypton.adapter.RosterAdapter
import cc.cryptopunks.crypton.chat.R
import cc.cryptopunks.crypton.context.Roster
import cc.cryptopunks.crypton.context.Service
import cc.cryptopunks.crypton.util.typedLog
import cc.cryptopunks.crypton.widget.ServiceLayout
import kotlinx.android.synthetic.main.roster.view.*
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.launch

class RosterView(
    context: Context
) : ServiceLayout(context) {

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

    override fun Service.Connector.connect(): Job = launch {
        log.d("bind")
        launch {
            input.filterIsInstance<Roster.Service.Items>().collect {
                log.d("receive: ${it.items.size}")
                rosterAdapter.submitList(it.items)
            }
        }
        launch {
            Roster.Service.GetItems.out()
        }
    }

    override fun onDetachedFromWindow() {
        coroutineContext.cancelChildren()
        super.onDetachedFromWindow()
    }
}