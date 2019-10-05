package cc.cryptopunks.crypton.util.reactivebindings

import android.widget.TextView
import cc.cryptopunks.kache.core.Kache
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.reactive.asFlow

suspend fun TextView.bind(property: Kache<String>) = property.asFlow().collect {
    text = it
}