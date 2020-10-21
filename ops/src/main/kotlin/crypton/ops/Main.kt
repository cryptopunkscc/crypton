package crypton.ops

import cc.cryptopunks.crypton.cli.CliContext
import cc.cryptopunks.crypton.cli.prepare
import cc.cryptopunks.crypton.cli.process
import java.io.File

fun main(args: Array<String>) {
    require(args.size > 1)
    CliContext(
        commands = commands,
        route = File(args.first()).normalize().absolutePath,
        isRoute = { this is String },
        empty = { "main" }
    ).prepare().process(args.drop(1).joinToString(" ")).run {
        println(result)
    }
}
