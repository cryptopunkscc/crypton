package cc.cryptopunks.crypton.util.reactivebindings

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import cc.cryptopunks.crypton.util.CacheFlow
import cc.cryptopunks.crypton.util.Input
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.reactive.asFlow
import org.reactivestreams.Publisher
import org.reactivestreams.Subscriber

private class TextChanges(
    private var editText: EditText
) : ViewPublisher<CharSequence>(), TextWatcher {

    override fun onSubscribed(subscriber: Subscriber<in CharSequence>) {
        editText.addTextChangedListener(this)
    }

    override fun onCanceled() {
        editText.removeTextChangedListener(this)
    }

    override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
        subscriber?.onNext(s)
    }

    override fun afterTextChanged(s: Editable?) {
        /*no-op*/
    }

    override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
        /*no-op*/
    }
}

fun EditText.textChangesPublisher(): Publisher<CharSequence> =
    TextChanges(this)

suspend fun EditText.bind(property: CacheFlow<Input>) {
    coroutineScope {
        var current: String? = null
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
            textChangesPublisher().asFlow().map { it.toString() }.collect { new ->
                if (text.toString() != current) {
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