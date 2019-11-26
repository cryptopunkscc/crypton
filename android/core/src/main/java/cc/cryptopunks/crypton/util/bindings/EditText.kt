package cc.cryptopunks.crypton.util.bindings

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import cc.cryptopunks.crypton.util.CacheFlow
import cc.cryptopunks.crypton.util.Input
import kotlinx.coroutines.channels.ProducerScope
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

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

suspend fun EditText.bind(property: CacheFlow<Input>) {
    coroutineScope {
        var current: CharSequence? = null
        launch {
            property.collect { input ->
                if (input.text != current) {
                    current = input.text
                    setText(input.text)
                }
                error = input.error.takeIf(String::isNotBlank)
            }
        }
        launch {
            textChanges().collect { new ->
                if (text != current) {
                    current = new
                    property {
                        copy(
                            text = new,
                            error = ""
                        )
                    }
                }
            }
        }
    }
}