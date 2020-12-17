package cc.cryptopunks.crypton.context

import cc.cryptopunks.crypton.dep
import cc.cryptopunks.crypton.util.OpenStore

val RootScope.clipboardStore: Clip.Board.Store by dep()
val RootScope.clipboardSys: Clip.Board.Sys by dep()
val RootScope.clipboardRepo: Clip.Board.Repo by dep()

class Clip(
    val id: Long = 0,
    val data: String = "",
    val timestamp: Long = System.currentTimeMillis()
) {
    object Board {

        class Store : OpenStore<List<Clip>>(emptyList())

        interface Sys {
            fun setClip(text: String)
        }

        interface Repo {
            suspend fun put(clip: Clip)
            suspend fun pop(): Clip?
        }
    }
}
