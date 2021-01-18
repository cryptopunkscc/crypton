package cc.cryptopunks.crypton.agent

import cc.cryptopunks.crypton.yaml.formatYaml
import cc.cryptopunks.crypton.yaml.parseYaml

data class Story(val map: Map<String, Any>) {
    val ver: Int by map
    val rel: List<String> by map
    val type: String by map

    init {
        ver; rel; type
    }
}


fun ByteArray.story() = Story(toString(Charsets.UTF_8).parseYaml())

fun Story.encode() = map.formatYaml().toByteArray(Charsets.UTF_8)
