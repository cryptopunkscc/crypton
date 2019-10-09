package cc.cryptopunks.crypton.util.reactivebindings

import cc.cryptopunks.crypton.util.CacheFlow
import cc.cryptopunks.crypton.util.Input
import com.google.android.material.textfield.TextInputLayout
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.reactive.asFlow

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
            editText!!.textChangesPublisher().asFlow().map { it.toString() }.collect { new ->
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