package cc.cryptopunks.crypton.util

class BroadcastError: Broadcast<Throwable>() {

    interface Core {
        val broadcastError: BroadcastError
    }
}