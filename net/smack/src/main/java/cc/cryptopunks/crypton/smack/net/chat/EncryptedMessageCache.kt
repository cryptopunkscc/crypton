package cc.cryptopunks.crypton.smack.net.chat

internal class EncryptedMessageCache {

    private val map = mutableMapOf<String, String>()

    operator fun set(stanzaId: String, message: String) = synchronized(this) {
        map[stanzaId] = message
    }

    operator fun get(stanzaId: String) = synchronized(this) {
        map.remove(stanzaId)
    }
}