package cc.cryptopunks.crypton.format

import cc.cryptopunks.crypton.cliv2.Cli

internal fun Cli.Execute.format(indent: String = INDENT): String = when (this) {
    is Cli.Commands -> "\n" + format(indent)
    is Cli.Command.Template -> format(indent)
    else -> toString()
}

private fun Cli.Commands.format(indent: String = INDENT): String = "\n" +
    (if (description == null) "" else "- $description\n\n") + (keyMap + matcherMap).map { (key: Any, value: Cli.Execute) ->
    "$indent$key ${value.format("$indent$INDENT")}"
}.joinToString("\n")

private const val INDENT = "    "

private fun Cli.Command.Template.format(indent: String = ""): String =
    (if (description == null) "" else "- $description") + params.format(indent) + "\n"

private fun List<Cli.Param>.format(indent: String = ""): String =
    joinToString("") { it.format(indent) }

fun Cli.Params.format() = list.format()

fun Cli.Param.format(indent: String = "") = when (val type = type) {
    is Cli.Param.Type.Config -> ":${type.key}"
    is Cli.Param.Type.Positional -> "<$name>"
    is Cli.Param.Type.Named -> "${type.name}=<$name>"
    is Cli.Param.Type.Text -> "<$name...>"
    is Cli.Param.Type.Option -> type.on + if (type.off == null) "" else "|${type.off}"
    else -> null
}?.let { param ->
    "\n$indent$param" + (description?.let { " - $it" } ?: "")
} ?: ""
