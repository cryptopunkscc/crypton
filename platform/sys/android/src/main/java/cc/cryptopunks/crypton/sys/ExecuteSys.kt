package cc.cryptopunks.crypton.sys

import cc.cryptopunks.crypton.context.Execute

internal object ExecuteSys : Execute.Sys {
    override fun invoke(execute: Execute): Int {
        throw UnsupportedOperationException("Executing shell commands is not supported on Android")
    }
}
