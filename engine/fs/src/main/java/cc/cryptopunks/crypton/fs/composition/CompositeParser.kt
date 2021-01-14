package cc.cryptopunks.crypton.fs.composition

import cc.cryptopunks.crypton.fs.Lore
import java.io.File

class CompositeParser(
    private val list: List<Lore.Parse>
) : Lore.Parse {
    constructor(vararg args: Lore.Parse) : this(args.toList())

    override fun can(file: File): Boolean = list.any { it.can(file) }
    override fun invoke(file: File): List<Lore> = list
        .filter { it.can(file) }
        .map { it(file) }.flatten()
}
