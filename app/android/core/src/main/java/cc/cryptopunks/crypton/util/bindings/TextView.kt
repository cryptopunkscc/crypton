package cc.cryptopunks.crypton.util.bindings

import android.view.KeyEvent
import android.widget.TextView
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

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
