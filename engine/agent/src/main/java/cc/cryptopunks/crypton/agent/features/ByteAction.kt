package cc.cryptopunks.crypton.agent.features

import cc.cryptopunks.crypton.agent.decodeDatagram
import cc.cryptopunks.crypton.create.resolver

fun decodeBytes() = resolver<ByteArray> { decodeDatagram(it) }
