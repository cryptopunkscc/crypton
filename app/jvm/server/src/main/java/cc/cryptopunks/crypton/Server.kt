package cc.cryptopunks.crypton

import cc.cryptopunks.crypton.backend.BackendService


class Server(
    val config: Config = Config().default().local(),
    val service: BackendService
) {
    class Config(
        map: Map<String, Any?> = emptyMap()
    ) : MutableMap<String, Any?> by map.toMutableMap() {
        var home: String by this
        var name: String by this
        var omemoStore: String by this
        var socketAddress: String by this
        var socketPort: Int by this
        var hostAddress: String? by this
        var securityMode: String by this
        var inMemory: String by this
    }
}
