package cc.cryptopunks.crypton.view

import android.content.Context
import android.view.View
import android.widget.FrameLayout
import androidx.recyclerview.widget.LinearLayoutManager
import cc.cryptopunks.crypton.adapter.RosterAdapter
import cc.cryptopunks.crypton.chat.R
import cc.cryptopunks.crypton.context.Actor
import cc.cryptopunks.crypton.context.Service
import cc.cryptopunks.crypton.presenter.RosterService.Output.RosterItemList
import kotlinx.android.synthetic.main.roster.view.*
import kotlinx.coroutines.cancelChildren

class RosterView(
    context: Context
) : FrameLayout(context),
    Service.Wrapper {

    private val scope = Actor.Scope()

    override val wrapper = wrapper(scope)

    private val rosterAdapter = RosterAdapter(scope)

    private val rosterItemDecorator = RosterItemDecorator(
        context = context,
        paddingLeft = resources.getDimensionPixelSize(R.dimen.roster_divider_padding_left),
        paddingRight = resources.getDimensionPixelSize(R.dimen.roster_divider_padding_right)
    )

    init {
        View.inflate(context, R.layout.roster, this)
        rosterRecyclerView.apply {
            addItemDecoration(rosterItemDecorator)
            layoutManager = LinearLayoutManager(context)
            adapter = rosterAdapter
        }
    }

    override fun onDetachedFromWindow() {
        scope.coroutineContext.cancelChildren()
        super.onDetachedFromWindow()
    }

    override suspend fun Any.onInput() = when (this) {
        is RosterItemList -> rosterAdapter.submitList(items)
        else -> null
    }
}