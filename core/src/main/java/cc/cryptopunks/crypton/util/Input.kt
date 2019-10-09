package cc.cryptopunks.crypton.util

data class Input(
    val text: String = "",
    val error: String = ""
)

var CacheFlow<Input>.text: String
    get() = value.text
    set(value) = invoke { copy(text = value, error = "") }

var CacheFlow<Input>.error: String
    get() = value.error
    set(value) = invoke { copy(error = value) }