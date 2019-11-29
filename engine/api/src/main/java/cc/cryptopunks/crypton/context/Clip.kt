package cc.cryptopunks.crypton.context

class Clip(
    val id: Long = 0,
    val data: String = "",
    val timestamp: Long = System.currentTimeMillis()
) {
    object Board {
        interface Sys {
            val setClip: SetClip

            interface SetClip : (String) -> Unit
        }

        interface Repo {
            suspend fun put(clip: Clip)
            suspend fun pop() : Clip?
        }
    }
}