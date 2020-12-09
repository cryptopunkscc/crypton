package cc.cryptopunks.crypton.context

import cc.cryptopunks.crypton.Scoped

data class Main(
    val type: Class<*>
) {
    interface Action : Scoped<RootScope>
}

data class ApplicationId(val value: String = "crypton")
