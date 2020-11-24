package cc.cryptopunks.crypton.service

import cc.cryptopunks.crypton.Features
import cc.cryptopunks.crypton.cliv2.Cli
import cc.cryptopunks.crypton.cliv2.commands

fun Features.cliCommands(): Cli.Commands = commands(
    args = commandTemplates().fold(mutableMapOf<String, Any>()) { acc, template ->
        acc.apply { put(template.name.split(" "), template) }
    }
)

private fun Features.commandTemplates(): List<Cli.Command.Template> = mapNotNull { it.command }

private tailrec fun MutableMap<String, Any>.put(
    keys: List<String>,
    template: Cli.Command.Template
) {
    when {
        keys.isEmpty() ->
            throw IllegalArgumentException("keys MUST have at least one value")
        keys.size == 1 ->
            getOrPut(keys.first()) { template }.let {
                require(it == template) {
                    "Trying to put $template but slot ${keys.first()} is already reserved for $it"
                }
            }

        else ->
            getOrPut(keys.first()) { mutableMapOf<String, Any>() }.let {
                require(it is MutableMap<*, *>) {
                    "Trying to put $template but slot ${keys.first()} is already reserved for $it"
                }
                it as MutableMap<String, Any>
            }.put(keys.drop(1), template)
    }
}
