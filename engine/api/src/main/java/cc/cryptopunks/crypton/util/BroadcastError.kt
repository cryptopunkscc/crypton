package cc.cryptopunks.crypton.util

class BroadcastError: Broadcast<Throwable>() {

    interface Component {
        val broadcastError: BroadcastError
    }

    class Module : Component {
        override val broadcastError = BroadcastError()
    }
}