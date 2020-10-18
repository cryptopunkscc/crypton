package cc.cryptopunks.crypton.view

import android.view.View
import cc.cryptopunks.crypton.util.Log
import cc.cryptopunks.crypton.widget.GenericViewHolder
import kotlinx.android.synthetic.main.log_event.view.*
import kotlin.properties.Delegates

internal class LogEventViewHolder(view: View) :
    GenericViewHolder<View>(view) {

    var item: Log.Event by Delegates.observable(Log.Event.Empty) { _, _, newValue ->
        newValue.set()
    }

    private fun Log.Event.set() {
        view.apply {
            logLabel.text = formatLabel()
            logMessage.text = message
            logError.text = throwable?.message
        }
    }

    private fun Log.Event.formatLabel() = listOf(
        label,
        "|",
        status,
        "|",
        action?.run { javaClass.name.removePackage().replace("$", ".") },
        scopes.takeIf { it.isNotEmpty() }?.run { ":(${scopes.joinToString("; ")})" },
        "|"
    ).joinToString(" ")
}

private fun String.removePackage() = replace("cc.cryptopunks.crypton.context.", "")
