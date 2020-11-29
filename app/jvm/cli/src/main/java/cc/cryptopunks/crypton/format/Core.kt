package cc.cryptopunks.crypton.format

import cc.cryptopunks.crypton.ActionFailed
import java.lang.StringBuilder

fun ActionFailed.format() = StringBuilder()
    .append(javaClass.name)
    .appendLine("action: $action")
    .appendLine("message: $message")
    .appendLine(stackTrace)
    .toString()
