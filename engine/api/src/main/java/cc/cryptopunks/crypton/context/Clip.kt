package cc.cryptopunks.crypton.context

import cc.cryptopunks.crypton.util.OpenStore

class Clip(
    val id: Long = 0,
    val data: String = "",
    val timestamp: Long = System.currentTimeMillis()
) {
    object Board {
        interface Sys {
            fun setClip(text: String)
        }

        class Store : OpenStore<List<Clip>>(emptyList())

        interface Repo {
            suspend fun put(clip: Clip)
            suspend fun pop(): Clip?
        }
    }
}
