package cc.cryptopunks.crypton.fs.parser.yml

import cc.cryptopunks.crypton.fs.Lore
import cc.cryptopunks.crypton.fs.util.isLore
import cc.cryptopunks.crypton.fs.util.parseYaml
import cc.cryptopunks.crypton.fs.util.toLore
import java.io.File

object YmlLoreParser : Lore.Parse {
    override fun can(file: File): Boolean = file.isLore()
    override fun invoke(file: File): List<Lore> = listOf(file.loreFromYaml())
}

fun File.loreFromYaml(): Lore =
    inputStream().parseYaml().toLore()
