package cc.cryptopunks.crypton.fs.repo

import cc.cryptopunks.crypton.fs.Id
import cc.cryptopunks.crypton.fs.Lore
import cc.cryptopunks.crypton.fs.Type
import cc.cryptopunks.crypton.fs.parser.yml.loreFromYaml
import cc.cryptopunks.crypton.fs.util.calculateId
import cc.cryptopunks.crypton.fs.util.formatYaml
import java.io.File

class FileLoreLore(
    dir: File
) : Lore.Repo {

    private val loreStorage = FileStorage(dir.resolve(LORE))
    private val refStorage = FileStorage(dir.resolve(REF))

    override suspend fun plus(lore: Lore): Id =
        lore.formatYaml().let { yaml ->
            yaml.calculateId().also { id ->
                loreStorage.new(id) { append(yaml) }
                lore.updateRelations(id)
            }
        }

    private fun Lore.updateRelations(loreId: String) {
        rel.map { id ->
            refStorage[id]
        }.filter { file ->
            if (file.exists()) loreId !in file.readLines().toSet()
            else file.createNewFile()
        }.forEach { file ->
            file.appendText("$loreId\n")
        }
    }

    override fun get(id: Id): Lore = loreStorage[id].loreFromYaml()

    override fun contains(id: Id): Boolean = id in loreStorage

    override fun minus(id: Id): Boolean = loreStorage - id

    override fun typeOf(any: Any): Boolean = any is Lore

    override fun refs(id: Id): Set<Lore> =
        refStorage[id].readLines().filter { contains(it) }.map { get(it) }.toSet()

    override fun getByType(types: Set<Type>): Set<Lore> =
        loreStorage.dir.listFiles()
            ?.mapNotNull { file ->
                file.loreFromYaml().takeIf { types.contains(it.type) }
            }
            ?.toSet()
            ?: emptySet()

    private companion object {
        const val LORE = "lore"
        const val REF = "ref"
    }
}
