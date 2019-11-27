package cc.cryptopunks.crypton.sys

import cc.cryptopunks.crypton.context.Network

data class NetworkStatus(
    val network: String? = null,
    val status: Network.Status
)