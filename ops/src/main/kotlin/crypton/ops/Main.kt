package crypton.ops

import cc.cryptopunks.crypton.translator.Context
import cc.cryptopunks.crypton.translator.prepare
import cc.cryptopunks.crypton.translator.process
import java.io.File

fun main(args: Array<String>) {
    require(args.size > 1)
    Context(
        commands = commands,
        route = File(args.first()).normalize().absolutePath,
        isRoute = { this is String },
        empty = { "main" }
    ).prepare().process(args.drop(1).joinToString(" ")).run {
        println(result)
    }
}
