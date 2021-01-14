package cc.cryptopunks.crypton.format

import cc.cryptopunks.crypton.Action

fun Action.Error.format() = StringBuilder()
    .append(javaClass.name)
    .appendLine("action: $action")
    .appendLine("message: $message")
    .appendLine(stackTrace)
    .toString()
