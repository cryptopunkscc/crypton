package cc.cryptopunks.crypton.util.bindings

import cc.cryptopunks.crypton.util.CacheFlow
import cc.cryptopunks.crypton.util.Input
import com.google.android.material.textfield.TextInputLayout
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

suspend fun TextInputLayout.bind(property: CacheFlow<Input>) {
    coroutineScope {
        var current: String? = null
        launch {
            property.collect { input ->
                if (input.text != current) {
                    current = input.text
                    editText!!.setText(input.text)
                    error = input.error.takeIf(String::isNotBlank)
                }
            }
        }
        launch {
            editText!!.textChanges().collect { new ->
                if (editText!!.text.toString() != current) {
                    current = new
                    property {
                        copy(text = new)
                    }
                }
            }
        }
    }
}