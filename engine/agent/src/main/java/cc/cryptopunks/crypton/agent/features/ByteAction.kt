package cc.cryptopunks.crypton.agent.features

import cc.cryptopunks.crypton.agent.decode
import cc.cryptopunks.crypton.create.resolver


fun decodeBytes() = resolver<ByteArray> { bytes -> bytes.decode() }
