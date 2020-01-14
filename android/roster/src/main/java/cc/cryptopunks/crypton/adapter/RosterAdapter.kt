package cc.cryptopunks.crypton.adapter

import android.view.ViewGroup
import cc.cryptopunks.crypton.context.Service
import cc.cryptopunks.crypton.util.PagedListServiceAdapter
import cc.cryptopunks.crypton.view.RosterItemView
import kotlinx.coroutines.CoroutineScope
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class RosterAdapter @Inject constructor(
    override val coroutineContext: CoroutineContext
) :
    PagedListServiceAdapter<RosterItemView, Service>(),
    CoroutineScope {

    private val dateFormat: DateFormat = SimpleDateFormat(
        "d MMM yyyy • HH:mm",
        Locale.getDefault()
    )

    override fun createView(
        parent: ViewGroup,
        viewType: Int
    ) = RosterItemView(parent.context, dateFormat)
}