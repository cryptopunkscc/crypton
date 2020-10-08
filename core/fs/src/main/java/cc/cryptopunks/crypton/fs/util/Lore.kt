package cc.cryptopunks.crypton.fs.util

import cc.cryptopunks.crypton.fs.Lore
import java.io.File
import java.io.Reader
import java.lang.StringBuilder

@Suppress("UNCHECKED_CAST")
fun Map<String, Any>.toLore(): Lore {
    val lore = get("lore") as Map<*, *>
    return Lore(
        type = requireNotNull(lore["type"] as? String),
        ver = requireNotNull(lore["ver"] as? Int),
        rel = requireNotNull(lore["rel"] as? List<String>).toSet(),
        body = minus("lore")
    )
}

fun Lore.formatYaml() = StringBuilder().apply {
    append(mapOf("lore" to headerToMap()).formatYaml())
    append(body.formatYaml())
}.toString()

fun Lore.calculateId() = formatYaml().calculateId()

fun Lore.toMap() = body + ("lore" to headerToMap())

internal fun Lore.headerToMap() = mapOf(
    "type" to type,
    "ver" to ver,
    "rel" to rel.toList(),
)

fun File.isLore(): Boolean = reader().isLore()

fun Reader.isLore(): Boolean {
    val buffer = CharArray(LORE_KEY.size)
    return (if (read(buffer) < LORE_KEY.size) false
    else buffer.contentEquals(LORE_KEY))
}

private val LORE_KEY = "lore:".toCharArray()
