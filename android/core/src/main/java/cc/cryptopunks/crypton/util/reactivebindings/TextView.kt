package cc.cryptopunks.crypton.util.reactivebindings

import android.view.KeyEvent
import android.widget.TextView
import cc.cryptopunks.kache.core.Kache
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.reactive.asFlow

suspend fun TextView.bind(property: Kache<String>) = property.asFlow().collect {
    text = it
}

fun TextView.flowEditorActions(): Flow<EditorAction> = callbackFlow {
    setOnEditorActionListener { view, actionId, event ->
        channel.offer(EditorAction(view, actionId, event))
    }
    awaitClose()
}

data class EditorAction(
    val view: TextView,
    val id: Int,
    val event: KeyEvent?
)