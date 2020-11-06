package cc.cryptopunks.crypton.util.bindings

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import kotlinx.coroutines.channels.ProducerScope
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

private class TextChanges(
    private val scope: ProducerScope<CharSequence>
) :
    TextWatcher {

    override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
        scope.channel.offer(s.toString())
    }

    override fun afterTextChanged(s: Editable?) {
        /*no-op*/
    }

    override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
        /*no-op*/
    }
}

fun EditText.textChanges(): Flow<CharSequence> = callbackFlow {
    val textChanges = TextChanges(this)
    addTextChangedListener(textChanges)
    awaitClose { removeTextChangedListener(textChanges) }
}
