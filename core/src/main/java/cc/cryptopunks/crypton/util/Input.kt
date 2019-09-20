package cc.cryptopunks.crypton.util

import cc.cryptopunks.kache.core.Kache
import cc.cryptopunks.kache.core.invoke

data class Input(
    val text: String = "",
    val error: String = ""
)

var Kache<Input>.text: String
    get() = value.text
    set(value) = invoke { copy(text = value) }