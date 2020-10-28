package cc.cryptopunks.crypton.format

import cc.cryptopunks.crypton.cliv2.Cli

internal fun Cli.Execute.format(indent: String = INDENT): String = when (this) {
    is Cli.Commands -> "\n" + format(indent)
//    is Cli.Command -> format(indent)
    is Cli.Command.Template -> format(indent)
    else -> toString()
}

private fun Cli.Commands.format(indent: String = INDENT): String = "\n" +
    (if (description == null) "" else "- $description\n\n") + (keyMap + matcherMap).map { (key: Any, value: Cli.Execute) ->
    "$indent$key ${value.format("$indent$INDENT")}"
}.joinToString("\n")

private const val INDENT = "    "

//private fun Cli.Command.format(indent: String = ""): String = "params: " + params.format(indent)

private fun Cli.Command.Template.format(indent: String = ""): String =
    (if (description == null) "" else "- $description") + params.format(indent) + "\n"

private fun List<Cli.Param>.format(indent: String = ""): String =
    joinToString("") { it.format(indent) }

private fun Cli.Param.format(indent: String = "") = when (val type = type) {
    is Cli.Param.Type.Positional -> "[$index]=<$name>"
    is Cli.Param.Type.Named -> "${type.name}=<$name>"
    is Cli.Param.Type.Text -> "<$name>"
    else -> null
}?.let {
    "\n$indent$it" + if (description != null)
        " - $description" else ""
} ?: ""
