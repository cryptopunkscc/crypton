package cc.cryptopunks.crypton.fs

import cc.cryptopunks.crypton.fs.repo.TypedRepo
import java.io.File

internal typealias Id = String
internal typealias Type = String
internal typealias Path = String

sealed class Data

data class Blob(
    val id: Id = "",
    val path: Path,
) : Data() {
    interface Repo : TypedRepo<Blob>
}

data class Lore(
    val ver: Int = 0,
    val type: Type = "",
    val rel: Set<Id> = emptySet(),
    val body: Map<String, Any> = emptyMap()
) : Data() {
    interface Repo : TypedRepo<Lore>, Relations
    interface Relations {
        fun refs(id: Id): Set<Lore>
        fun getByType(types: Set<Type>): Set<Lore>
    }

    interface Parse {
        fun can(file: File): Boolean
        operator fun invoke(file: File): List<Lore>
    }
}
