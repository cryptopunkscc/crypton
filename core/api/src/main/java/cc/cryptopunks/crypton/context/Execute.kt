package cc.cryptopunks.crypton.context

data class Execute(
    val command: String,
    val args: List<String>
) {
    interface Sys : (Execute) -> Int
}
