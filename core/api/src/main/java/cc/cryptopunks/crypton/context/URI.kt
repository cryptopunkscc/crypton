package cc.cryptopunks.crypton.context

import java.io.File

data class URI(
    val path: String
) {
    data class Data(
        val scheme: String? = null,
        val authority: String? = null,
        val path: String? = null,
        val query: String? = null,
        val fragment: String? = null
    ) {
        override fun toString() = format()
    }

    interface Sys {
        fun resolve(uri: URI): File
    }
}

fun String.parseUriData() {
    java.net.URI.create(this).run {
        URI.Data(
            scheme = scheme,
            authority = authority,
            path = path,
            query = query,
            fragment= fragment,
        )
    }
}

fun URI.Data.format() = listOfNotNull(
    scheme?.let { "$it:" },
    authority?.let { "//$it" },
    path?.let { "/$it" },
    query?.let { "?$it" },
    fragment?.let { "#$it" }
).joinToString("")
