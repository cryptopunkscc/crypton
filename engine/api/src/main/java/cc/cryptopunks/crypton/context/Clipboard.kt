package cc.cryptopunks.crypton.context

object Clipboard {
    interface Sys {
        interface SetClip : (String) -> Unit
    }
}