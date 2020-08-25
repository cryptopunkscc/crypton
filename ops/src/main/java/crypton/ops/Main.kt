package crypton.ops

import cc.cryptopunks.crypton.translator.Context
import cc.cryptopunks.crypton.translator.prepare
import cc.cryptopunks.crypton.translator.process

fun main(args: Array<String>) {
    Context(commands).prepare().process(args.joinToString(" ")).run {
        println(result)
    }
}
