package crypton.ops

import cc.cryptopunks.crypton.cliv2.Cli
import cc.cryptopunks.crypton.cliv2.cliConfig
import cc.cryptopunks.crypton.cliv2.reduce
import java.io.File

fun main(args: Array<String>) {
    require(args.size > 1)
    Cli.Context(
        commands = commands,
        config = cliConfig(
            "path" to File(args.first()).normalize().absolutePath
        )
    ).reduce(args.drop(1).joinToString(" ")).run {
        println(result)
    }
}
