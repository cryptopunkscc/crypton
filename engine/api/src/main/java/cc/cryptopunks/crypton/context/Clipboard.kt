package cc.cryptopunks.crypton.context

object Clipboard {
    interface Sys {
        val setClip: SetClip

        interface SetClip : (String) -> Unit
    }
}