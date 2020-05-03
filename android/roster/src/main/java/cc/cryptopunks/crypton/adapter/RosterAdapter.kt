package cc.cryptopunks.crypton.adapter

import android.view.ViewGroup
import cc.cryptopunks.crypton.context.Connectable
import cc.cryptopunks.crypton.util.PagedListServiceAdapter
import cc.cryptopunks.crypton.view.RosterItemView
import kotlinx.coroutines.CoroutineScope
import java.text.SimpleDateFormat
import java.util.*
import kotlin.coroutines.CoroutineContext

class RosterAdapter(
    override val coroutineContext: CoroutineContext
) :
    PagedListServiceAdapter<RosterItemView, Connectable>(),
    CoroutineScope {

    private val dateFormat = SimpleDateFormat(
        "d MMM yyyy • HH:mm",
        Locale.getDefault()
    )

    override fun createView(
        parent: ViewGroup,
        viewType: Int
    ) = RosterItemView(parent.context, dateFormat)
}
