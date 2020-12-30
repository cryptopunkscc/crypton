package cc.cryptopunks.crypton.context

import cc.cryptopunks.crypton.dep

val RootScope.mainClass: Main by dep()

data class Main(
    val type: Class<*>,
) {
    interface Action : cc.cryptopunks.crypton.Action
}
