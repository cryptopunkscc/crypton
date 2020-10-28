package cc.cryptopunks.crypton.core.cliv2

import cc.cryptopunks.crypton.cliv2.Cli
import cc.cryptopunks.crypton.cliv2.prepare

internal class MutableConfig(
    map: Map<String, Any?>
): MutableMap<String, Any?> by map.toMutableMap() {
    var account: String? by this
    var chat: String? by this
}

internal fun Cli.Context.configure(
    block: MutableConfig.() -> Unit
): Cli.Context =
    MutableConfig(config.map).apply(block).let { map ->
        prepare().copy(config = config.copy(map = map))
    }

