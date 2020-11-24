package cc.cryptopunks.crypton.cli

import cc.cryptopunks.crypton.cliv2.Cli

internal class MutableConfig(
    map: Map<String, Any?>
): MutableMap<String, Any?> by map.toMutableMap() {
    var account: String? by this
    var chat: String? by this
}

internal fun Cli.Context.configure(
    block: MutableConfig.() -> Unit
): Cli.Context =
    copy(
        config = config.copy(map = MutableConfig(config.map).apply(block)),
        result = Cli.Result.Return(Unit)
    )
