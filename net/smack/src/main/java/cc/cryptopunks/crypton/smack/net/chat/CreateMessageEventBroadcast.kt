package cc.cryptopunks.crypton.smack.net.chat

import org.jivesoftware.smack.packet.Message

internal enum class MessageType {
    Incoming,
    Outgoing,
    CarbonCopy
}

internal typealias MessageEvent = Pair<Message, MessageType>
