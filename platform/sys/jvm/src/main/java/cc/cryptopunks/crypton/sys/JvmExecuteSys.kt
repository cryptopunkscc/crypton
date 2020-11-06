package cc.cryptopunks.crypton.sys

import cc.cryptopunks.crypton.context.Execute

internal object JvmExecuteSys : Execute.Sys {
    override fun invoke(execute: Execute): Int =
        Runtime.getRuntime().exec(execute.formatCommand()).waitFor()
}

private fun Execute.formatCommand() = command + " " + args.joinToString(" ")
