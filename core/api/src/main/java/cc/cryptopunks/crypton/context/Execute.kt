package cc.cryptopunks.crypton.context

import cc.cryptopunks.crypton.delegate.dep

val RootScope.executeSys: Execute.Sys by dep()

data class Execute(
    val command: String,
    val args: List<String>,
) {
    interface Sys : (Execute) -> Int
}
