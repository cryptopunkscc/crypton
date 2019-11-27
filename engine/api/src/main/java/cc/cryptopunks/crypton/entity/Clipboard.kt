package cc.cryptopunks.crypton.entity

object Clipboard {
    interface Sys {
        interface SetClip : (String) -> Unit
    }
}